package com.sbsj.mydrawconfrence

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

open class PaintView(context: Context?) : View(context) {

    var paint = Paint()
    var mCanvas = Canvas()
    var pathList = mutableListOf<Path>()
    var undoPathList = mutableListOf<Path>()

    private var path = Path()
    private var x: Int = 0
    private var y: Int = 0
    var colorValue = 0


    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.STROKE // todo  선 모양 지정 함수
        paint.strokeWidth = 10.0f // todo 선 굵기 지정 함수

        for (p in pathList) {
            canvas!!.drawPath(p, paint)
        }

        canvas!!.drawPath(path,paint)


    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        x = event.x.toInt()
        y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                startDrawing()
            MotionEvent.ACTION_MOVE ->
                checkCurrentPosition(event)
            MotionEvent.ACTION_UP ->
                finishDrawing()
        }




        return true
    }

    private fun checkCurrentPosition(event: MotionEvent) {
        x = event.x.toInt()
        y = event.y.toInt()
        path.lineTo(x.toFloat(), y.toFloat())
        invalidate() // onDraw 화면갱신함수

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
    private fun startDrawing(){
        path.reset()
        path.moveTo(x.toFloat(), y.toFloat())
        invalidate()
    }
    private fun finishDrawing() {


        pathList.add(path)
        path = Path()
        invalidate()


    }

    fun prevFunction() {
        if (pathList.size > 0) {
            undoPathList.add(
                pathList
                    .removeAt(pathList.size - 1)
            )
            invalidate();
        }
    }
    fun nextFunction(){
        if (undoPathList.size > 0) {
            pathList.add(
                undoPathList
                    .removeAt(undoPathList.size - 1)
            )
            invalidate();
        }

    }
}