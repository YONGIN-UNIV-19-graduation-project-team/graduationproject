package com.nyj.routinemaker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable

class RectOverlay(context: Context?, @Nullable attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint: Paint
    fun drawOverlay(rect : Rect?, canvas: Canvas) {
        canvas.drawRect(
            rect!!.left.toFloat(),
            rect.top.toFloat(),
            rect.right.toFloat(),
            rect.bottom.toFloat(),
            paint
        )
        invalidate()
    }

    init {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.FILL
    }
}