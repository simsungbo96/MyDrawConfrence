package com.sbsj.mydrawconfrence

import android.content.Context
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initPaintView()

        activityMainBinding.ClearDrawingButton.setOnClickListener {
            clearDrawing()
        }
        activityMainBinding.PrevCancelButton.setOnClickListener {
            paintView.prevFunction()
        }
        activityMainBinding.NextActionButton.setOnClickListener {
            paintView.nextFunction()
        }

        activityMainBinding.DrawChangeButton.setOnClickListener {
            paintView.plusChangeValue()
            activityMainBinding.DrawChangeButton.setBackgroundColor(paintView.changeDrawColor())
        }
    }

    //페인트 뷰 초기화
    private fun initPaintView() {
        paintView = PaintView(activityMainBinding.root.context)
        activityMainBinding.DrawChangeButton.setBackgroundColor(paintView.changeDrawColor())
        activityMainBinding.root.addView(paintView)

    }


//
//    // 체인지 버튼 기능
//    private fun initChangeDrawButton() {
//        activityMainBinding.DrawChangeButton.setOnClickListener {
//
//            paintView.colorValue++
//            currentValue = paintView.colorValue
//            paintView = PaintView(activityMainBinding.root.context)
//            paintViewList.add(paintView)
//            paintView.colorValue = currentValue
//            recordPaintOrder = paintViewList.size
//            paintView.changeDrawColor()
//            activityMainBinding.root.addView(paintView)
//            activityMainBinding.DrawChangeButton.setBackgroundColor(
//                setChangeDrawButtonColor(
//                    paintView.colorValue
//                )
//            )
//
//            Log.d("TAG", "initChangeDrawButton: ${paintViewList.size}")
//
//        }
//    }
//


//

//
//    private fun nextAction() {
//
//        if (recordPaintOrder > paintViewList.size - 1 - recordPaintOrder) {
//            recordPaintOrder = paintViewList.size - 1
//            return
//        }
//
//        activityMainBinding.root.addView(paintViewList[recordPaintOrder])
//        activityMainBinding.DrawChangeButton.setBackgroundColor(
//            setChangeDrawButtonColor(
//                currentValue
//            )
//        )
//        recordPaintOrder += 1
//        createPaintView()
//    }

    private fun clearDrawing() {
        paintView.pathList.clear()
        activityMainBinding.root.removeView(paintView)
        initPaintView()


    }

    //todo 현재뷰 위치 vs 현재뷰위치기준으로 몇번쨰 인덱스 인지 찾기
}