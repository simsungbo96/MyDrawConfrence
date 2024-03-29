package com.sbsj.mydrawconfrence

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

open class PaintView(context: Context?) : View(context) {

    var currentColor = mutableListOf<Paint>()
    var undoColor = mutableListOf<Paint>()

    var paint = Paint()


    var mCanvas = Canvas()
    var pathList = mutableListOf<Path>()
    private var undoPathList = mutableListOf<Path>()

    private var path = Path()
    private var x: Int = 0
    private var y: Int = 0
    private var colorValue = 0
    private var strokeValue = 0
    var selectColor = 0
    var selectStroke = 10.0f


    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.STROKE // todo  선 모양 지정 함수



        for (p in pathList.indices) {

            if (currentColor.isEmpty()) {
                canvas!!.drawPath(pathList[p], paint)
            } else {
                canvas!!.drawPath(pathList[p], currentColor[p])
            }


        }

        canvas!!.drawPath(path, paint)
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

    fun plusChangeColorValue() {
        colorValue++
    }

    fun plusChangeStrokeValue() {
        strokeValue++

    }

    fun changeDrawStroke(): Float {
        if (strokeValue > 4) {
            strokeValue = 0
        }
        when (strokeValue) {
            0 -> selectStroke = 10.0f
            1 -> selectStroke = 20.0f
            2 -> selectStroke = 30.0f
            3 -> selectStroke = 40.0f
            4 -> selectStroke = 5.0f
        }
        return selectStroke
    }

    fun changeDrawColor(): Int {
        if (colorValue > 4) {
            colorValue = 0
        }
        when (colorValue) {
            0 -> selectColor = Color.BLACK
            1 -> selectColor = Color.RED
            2 -> selectColor = Color.GREEN
            3 -> selectColor = Color.BLUE
            4 -> selectColor = Color.YELLOW
        }
        return selectColor
    }


    private fun startDrawing() {

        paint.color = selectColor
        paint.style = Paint.Style.STROKE // todo  선 모양 지정 함수
        paint.strokeWidth = selectStroke // todo 선 굵기 지정 함수
        val tempColor = Paint()
        tempColor.color = selectColor
        tempColor.style = Paint.Style.STROKE // todo  선 모양 지정 함수
        tempColor.strokeWidth = selectStroke // todo 선 굵기 지정 함수
        currentColor.add(tempColor)

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
            undoColor.add(
                currentColor.removeAt(currentColor.size - 1)
            )
            invalidate();
        }
    }

    fun nextFunction() {
        if (undoPathList.size > 0) {
            pathList.add(
                undoPathList
                    .removeAt(undoPathList.size - 1)
            )
            currentColor.add(
                undoColor
                    .removeAt(undoColor.size - 1)
            )
            invalidate();
        }

    }
}