package com.example.android.minipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs


const val STROKE_WIDTH = 12f// Must be a float

class MyCanvasView(context: Context):View(context){

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private lateinit var frame: Rect

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private val backGroundColor = ResourcesCompat.
                                    getColor(resources,R.color.colorBackground, null )
    private val drawColor = ResourcesCompat.
                                    getColor(resources, R.color.colorPaint, null)
    private val paint = Paint().apply {
        color = drawColor //Color for painting
        isAntiAlias = true //Smooths out the edges of what is to be drawn without affecting shape
        isDither = true // Affects how colors in device with higher precision are down sampled
        style = Paint.Style.STROKE// default is always fill
        strokeJoin = Paint.Join.ROUND // default is Miter
        strokeCap = Paint.Cap.ROUND //default is Butt
        strokeWidth = STROKE_WIDTH // default is really thin: Hairline Width
    }
    private val path = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backGroundColor)

        val inset = 40
        frame = Rect(inset, inset, width - inset, height- inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
       canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
        canvas?.drawRect(frame, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when(event.action){
            MotionEvent.ACTION_UP -> touchUp()
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
        }
        return true
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - motionTouchEventY)
        val dy = abs(motionTouchEventY - motionTouchEventX)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchUp() {
        path.reset()
    }
}