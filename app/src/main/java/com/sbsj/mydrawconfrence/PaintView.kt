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

open class PaintView(context: Context?)  :  View(context) {

    private val paint = Paint()
    private var path = Path()
    private var x: Int = 0
    private var y: Int = 0
    private var colorValue = 0

    enum class AbleColor {
        BLACK,  // 안전
        RED,  // 관심
        GREEN,  // 주의
        BLUE,  // 경고
    }


    override fun onDraw(canvas: Canvas?) {

        changeDrawColor()  // todo 색 지정 함수
        paint.style = Paint.Style.STROKE // todo  선 모양 지정 함수
        paint.strokeWidth = 3.0f // todo 선 굵기 지정 함수

        canvas?.let {
            canvas.drawPath(path, paint)
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
        when (colorValue) {
            0 -> paint.color = Color.BLACK
            1 -> paint.color = Color.RED
            2 -> paint.color = Color.GREEN
            3 -> paint.color = Color.BLUE
        }

    }

}