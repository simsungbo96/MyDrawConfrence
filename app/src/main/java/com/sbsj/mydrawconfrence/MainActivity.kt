package com.sbsj.mydrawconfrence

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.sbsj.mydrawconfrence.databinding.ActivityMainBinding

//todo 1. 색변경시 paint 객체 생성 대신 그리고나서  paint 객체생성
//todo 2. UserSelectTool 에 유저가 선택한 도구들을 저장하는 데이터 클래스 구현 필요
//todo 3. 한번 그릴떄 객체 생성하게 수정.
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    lateinit var paintView: PaintView

    private var paintViewList = mutableListOf<PaintView>()
    private var recordPaintOrder = 0
    private var currentValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initPaintView()
        initChangeDrawButton()

        activityMainBinding.ClearDrawingButton.setOnClickListener {
            clearDrawing()
        }
        activityMainBinding.PrevCancelButton.setOnClickListener {
            prevCancel()
        }
        activityMainBinding.NextActionButton.setOnClickListener {
            nextAction()
        }
    }

    //페인트 뷰 초기화
    private fun initPaintView() {
        paintView = PaintView(activityMainBinding.root.context)
        paintView.changeDrawColor()
        activityMainBinding.root.addView(paintView)
        currentValue = paintView.colorValue
        paintViewList.add(paintView)
    }

    //페인트 뷰 새로 만들거나 전부 지울때 사용하는 함수. (기존 도구 값을 가져가야함) todo useSelectTool 데이터 클래스 구현 필요
    private fun createPaintView() {
        paintView = PaintView(activityMainBinding.root.context)
        activityMainBinding.root.addView(paintView)
        paintView.paint.color = setChangeDrawButtonColor(currentValue)



    }

    // 체인지 버튼 기능
    private fun initChangeDrawButton() {
        activityMainBinding.DrawChangeButton.setOnClickListener {

            paintView.colorValue++
            currentValue = paintView.colorValue
            paintView = PaintView(activityMainBinding.root.context)
            paintViewList.add(paintView)
            paintView.colorValue = currentValue
            recordPaintOrder = paintViewList.size
            paintView.changeDrawColor()
            activityMainBinding.root.addView(paintView)
            activityMainBinding.DrawChangeButton.setBackgroundColor(
                setChangeDrawButtonColor(
                    paintView.colorValue
                )
            )

            Log.d("TAG", "initChangeDrawButton: ${paintViewList.size}")

        }
    }

    //체인지버튼 색 적용 >> colorValue 에 따라
    private fun setChangeDrawButtonColor(color: Int): Int {
        return when (color) {
            0 -> Color.BLACK
            1 -> Color.RED
            2 -> Color.GREEN
            3 -> Color.WHITE
            else -> Color.BLACK
        }
    }

    private fun clearDrawing() {
        for (number in 0 until paintViewList.size) {
            activityMainBinding.root.removeView(paintViewList[number])
        }
        paintViewList.clear()
        recordPaintOrder = paintViewList.size
        createPaintView()
    }

    private fun prevCancel() {
        recordPaintOrder -= 1
        if (recordPaintOrder <= -1) {
            recordPaintOrder = 0
            return
        }

        activityMainBinding.root.removeView(paintViewList[recordPaintOrder])
        activityMainBinding.DrawChangeButton.setBackgroundColor(
            setChangeDrawButtonColor(
                currentValue
            )
        )
        createPaintView()
    }

    private fun nextAction() {

        if (recordPaintOrder > paintViewList.size-1 ) {
            recordPaintOrder = paintViewList.size-1
            return
        }

        activityMainBinding.root.addView(paintViewList[recordPaintOrder])
        activityMainBinding.DrawChangeButton.setBackgroundColor(
            setChangeDrawButtonColor(
                currentValue
            )
        )
        recordPaintOrder += 1
        createPaintView()
    }

    //todo 현재뷰 위치 vs 현재뷰위치기준으로 몇번쨰 인덱스 인지 찾기
}