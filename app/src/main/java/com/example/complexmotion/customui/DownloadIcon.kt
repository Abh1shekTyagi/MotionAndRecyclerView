package com.example.complexmotion.customui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.complexmotion.R

class DownloadIcon @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0) :
    View(context, attrs,defStyleRes) {
    private val clippedPath = Path()
    private val paintColor = context.getColor(R.color.helioTrope)
    private var clippedRectangleWidth = 0f
    private var clippedRectangleHeight = 0f
    private var clippedRectangleLeft = 0f
    private var clippedRectangleRight = 0f
    private var clippedRectangleTop = 0f
    private var clippedRectangleBottom = 0f
    private val rect = RectF()
    private var horizontalCenter = 0f
    private var verticalCenter = 0f
    private var actionUp = true
    private val paint = Paint().apply {
        color = paintColor
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (actionUp) {
            clippedRectangleWidth = width / 10f
            clippedRectangleHeight = height / 7f
        }
        canvas.save()
        canvas.translate(horizontalCenter, verticalCenter)
        clipDownloadIcon(canvas)
        paint.color = paintColor
        canvas.drawCircle(
            width / 2f,
            height / 2f + clippedRectangleWidth / 5,
            clippedRectangleHeight * 2.5f,
            paint
        )
        canvas.restore()
    }


    private fun clipDownloadIcon(canvas: Canvas) {
        clippedRectangleLeft = (width - clippedRectangleWidth) / 2
        clippedRectangleRight = (width + clippedRectangleWidth) / 2
        clippedRectangleTop = (height - clippedRectangleHeight) / 2
        clippedRectangleBottom = (height + clippedRectangleHeight) / 2

        rect.set(
            clippedRectangleLeft,
            clippedRectangleTop,
            clippedRectangleRight,
            clippedRectangleBottom
        )

        clippedPath.addRect(rect, Path.Direction.CCW)

        clippedPath.moveTo(clippedRectangleLeft - clippedRectangleWidth / 2, clippedRectangleBottom)
        clippedPath.lineTo(width / 2f, clippedRectangleBottom + clippedRectangleHeight)
        clippedPath.lineTo(
            clippedRectangleRight + clippedRectangleWidth / 2, clippedRectangleBottom
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipPath(clippedPath, Region.Op.DIFFERENCE)
        } else {
            canvas.clipOutPath(clippedPath)
        }
        clippedPath.reset()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    horizontalCenter = 0f
                    verticalCenter = 0f
                    actionUp = true
                }

                else -> {
                    horizontalCenter = event.x - width / 2
                    verticalCenter = event.y - height / 2
                    clippedRectangleWidth = width / 23f
                    clippedRectangleHeight = height / 20f
                    actionUp = false
                }
            }
        }
        invalidate()
        return true
    }
}