package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes


class MultiColorBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val sections: MutableList<ConvertedSection> = mutableListOf()
    private val paint: Paint = Paint().apply {
        style = Paint.Style.FILL

    }

    fun setSections(sections: List<Section>) {
        // get total
        var total = 0.0
        sections.forEach {
            total += it.size
        }

        this.sections.clear()
        this.sections.addAll(
            sections
                .map {
                    @Suppress("DEPRECATION")
                    ConvertedSection(it.size.div(total), context.resources.getColor(it.color))
                }
        )

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
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

    data class Section(val size: Double, @ColorRes val color: Int)
    private class ConvertedSection(val percentage: Double, @ColorInt val color: Int)
}