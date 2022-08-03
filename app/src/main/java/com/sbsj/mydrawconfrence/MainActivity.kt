package com.sbsj.mydrawconfrence

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.icu.text.SimpleDateFormat
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import com.sbsj.mydrawconfrence.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.util.*


//todo 1. 색변경시 paint 객체 생성 대신 그리고나서  paint 객체생성 (완)
//todo 2. UserSelectTool 에 유저가 선택한 도구들을 저장하는 데이터 클래스 구현 필요 (현재 불필요)
//todo 3. 한번 그릴떄 객체 생성하게 수정.(완)

class MainActivity : AppCompatActivity(),HBRecorderListener {


    private lateinit var activityMainBinding: ActivityMainBinding
    lateinit var paintView: PaintView
    lateinit var appPermission: AppPermission

    lateinit var mediaRecorder: MediaRecorder
    var isRecording = false
    var notFirst = 0
    private  val FOREGROUND_SERVICE_ID = 1000


    private val REQUEST_CODE = 1000
    private val REQUEST_PERMISSION = 1001
    private val ORIENTATION = SparseIntArray()

    private lateinit var mediaProjection: MediaProjection
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private lateinit var virtualDisplay: VirtualDisplay

    private lateinit var mediaRecorder2: MediaRecorder


    private var mScreenDensity: Int = 0
    private val DISPLAY_WIDTH = 720
    private var DISPLAY_HEIGHT = 1280


    private lateinit var hbRecord : HBRecorder

    init {
        ORIENTATION.append(Surface.ROTATION_0, 90)
        ORIENTATION.append(Surface.ROTATION_90, 0)
        ORIENTATION.append(Surface.ROTATION_180, 270)
        ORIENTATION.append(Surface.ROTATION_270, 90)
    }

    private var videoUri = ""


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initHBRecorder()

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        mScreenDensity = metrics.densityDpi
        mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)



        initPaintView()
        requestAppPermission()



        activityMainBinding.ClearDrawingButton.setOnClickListener { clearDrawing() }
        activityMainBinding.PrevCancelButton.setOnClickListener { paintView.prevFunction() }
        activityMainBinding.NextActionButton.setOnClickListener { paintView.nextFunction() }
        activityMainBinding.DrawStrokeChangeButton.setOnClickListener {
            paintView.plusChangeStrokeValue()
            paintView.changeDrawStroke()
             }
        activityMainBinding.DrawChangeButton.setOnClickListener {
            paintView.plusChangeColorValue()
            activityMainBinding.DrawChangeButton.backgroundTintList =
                ColorStateList.valueOf(paintView.changeDrawColor())
        }

        activityMainBinding.RecordButton.setOnClickListener {
            activityMainBinding.RecordButton.visibility =  View.INVISIBLE
            activityMainBinding.StopButton.visibility =  View.VISIBLE
            initHBRecorder()
           startRecordingScreen()
        }

        activityMainBinding.StopButton.setOnClickListener {
            activityMainBinding.StopButton.visibility =  View.INVISIBLE
            activityMainBinding.RecordButton.visibility =  View.VISIBLE
          hbRecord.stopScreenRecording()
        }

    }


//todo 버튼 초기화

    //페인트 뷰 초기화
    private fun initPaintView() {
        paintView = PaintView(activityMainBinding.root.context)
        activityMainBinding.DrawChangeButton.backgroundTintList =
            ColorStateList.valueOf(paintView.changeDrawColor())
        activityMainBinding.root.addView(paintView)

    }

    private fun clearDrawing() {
        paintView.pathList.clear()
        activityMainBinding.root.removeView(paintView)
        initPaintView()

    }

    //권한 요청
    private fun requestAppPermission() {
        appPermission = AppPermission(activity = this)
        val permission = mutableListOf(Manifest.permission.RECORD_AUDIO)
        appPermission.check(permission = permission)

    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun createRecorder() {
        mediaRecorder = MediaRecorder()
        val path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        var currentTime = dateName()
        var file = File(path, "$currentTime.mp4")
        try {
            path.mkdirs()
        } catch (e: IOException) {
            return

        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        var metrix = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrix)
        mediaRecorder.setVideoSize(metrix.widthPixels, metrix.heightPixels)
        mediaRecorder.setVideoEncodingBitRate(512 * 1000)
        mediaRecorder.setVideoFrameRate(30)
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(file.absolutePath)
        mediaRecorder.prepare()


    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initHBRecorder(){
        hbRecord = HBRecorder(this,this)
        hbRecord.fileName = dateName()
    }

    private fun startRecordingScreen(){
        val mediaProjection = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val permissionIntent = mediaProjection.createScreenCaptureIntent()
        startActivityForResult(permissionIntent,1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==1){
            if(resultCode == RESULT_OK){
                hbRecord.startScreenRecording(data,resultCode)
            }
        }
    }

    override fun HBRecorderOnStart() {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun HBRecorderOnComplete() {
        hbRecord.fileName = dateName()
        val path =  Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )



        hbRecord.setOutputPath(path.toString())
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun dateName(): String {
        var now = System.currentTimeMillis();

        val date = Date(now)
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH.mm.ss")
        return  return "DC_"+dateFormat.format(date)
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String?) {

    }


}





