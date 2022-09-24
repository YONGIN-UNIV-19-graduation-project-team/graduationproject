package com.nyj.routinemaker

import android.content.Context
import android.gesture.OrientedBoundingBox
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.core.graphics.plus
import androidx.core.graphics.times

class RectOverlay(context: Context?, @Nullable attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint: Paint
    private var boundingBox:Rect = Rect()

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        canvas?.drawRect(boundingBox,paint)

        println("left : "+boundingBox.left+"| top : "+boundingBox.top+"| right : "+boundingBox.right+"| bottom : "+boundingBox.bottom)
    }

    init {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }
    fun drawBoundingBox(boundingBox: Rect){
        this.boundingBox = boundingBox


        invalidate()
    }

}
