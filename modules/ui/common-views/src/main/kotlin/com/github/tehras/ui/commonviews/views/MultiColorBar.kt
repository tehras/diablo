package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet


class MultiColorBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MultiColorLayout(context, attrs, defStyleAttr) {
    private val paint: Paint = Paint().apply {
        style = Paint.Style.FILL
    }

    override fun drawColors(canvas: Canvas) {
        if (sections.isEmpty()) return

        var startX = 0f //start from left

        paint.strokeWidth = measuredHeight.toFloat()

        sections.forEach {
            paint.color = it.color
            val lengthX = measuredWidth.times(it.percentage)

            canvas.drawRoundRect(
                startX,
                measuredHeight.toFloat(),
                startX.plus(lengthX).toFloat(),
                0f,
                measuredHeight.div(2).toFloat(),
                measuredHeight.div(2).toFloat(),
                paint
            )
            startX += lengthX.toFloat()
        }
    }
}