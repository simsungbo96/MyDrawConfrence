package com.sbsj.mydrawconfrence

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import kotlin.math.log

open class PaintView(context: Context?) : View(context) {

    var paint = Paint()
    var paintViewList = mutableListOf<Paint>()
    var pathList = mutableListOf<Path>()

    var preCancel = false
    var preCancelCount = 0
    private var path = Path()
    private var x: Int = 0
    private var y: Int = 0
    var colorValue = 0


    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.STROKE // todo  선 모양 지정 함수
        paint.strokeWidth = 10.0f // todo 선 굵기 지정 함수
        if (preCancel) {
            canvas?.let {
                if(preCancelCount < 0  ){
                    preCancelCount = paintViewList.size -1
                    return
                }
                for (number in 0 until preCancelCount) {
                    canvas.drawPath(pathList[number] ,paintViewList[number])
                }

            }
            preCancel = false
        } else {
            canvas?.let {
                canvas.drawPath(path, paint)
                Log.d("TAG", "onTouchEvent: " + paintViewList.size)
            }
        }


    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        x = event.x.toInt()
        y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                path.moveTo(x.toFloat(), y.toFloat())
            MotionEvent.ACTION_MOVE ->
                checkCurrentPosition(event)
            MotionEvent.ACTION_UP ->
                finishDrawing()


        }

        invalidate() // onDraw 화면갱신함수


        return true
    }

    private fun checkCurrentPosition(event: MotionEvent) {
        x = event.x.toInt()
        y = event.y.toInt()
        path.lineTo(x.toFloat(), y.toFloat())


    }

    fun changeDrawColor() {
        if (colorValue > 3) {
            colorValue = 0
        }
        when (colorValue) {
            0 -> paint.color = Color.BLACK
            1 -> paint.color = Color.RED
            2 -> paint.color = Color.GREEN
            3 -> paint.color = Color.WHITE
        }
    }

    fun finishDrawing() {
        paintViewList.add(paint)
        pathList.add(path)
        preCancelCount = paintViewList.size
        paint = Paint()
        path = Path()
    }

    fun prevCancel() {
        preCancel = true
        preCancelCount -= 1
        if(preCancelCount < 0){
            preCancelCount = 0
        }
        invalidate()

//        recordPaintOrder -= 1
//        if (recordPaintOrder < 0) {
//            recordPaintOrder = 0
//            return
//        }


    }

    //페인트 뷰 새로 만들거나 전부 지울때 사용하는 함수. (기존 도구 값을 가져가야함) todo useSelectTool 데이터 클래스 구현 필요

}