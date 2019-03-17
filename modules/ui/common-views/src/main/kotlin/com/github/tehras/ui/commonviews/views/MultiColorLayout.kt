package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes


abstract class MultiColorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    protected val sections: MutableList<ConvertedSection> = mutableListOf()

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
        drawColors(canvas)
    }

    protected abstract fun drawColors(canvas: Canvas)

    data class Section(val size: Double, @ColorRes val color: Int)
    protected class ConvertedSection(val percentage: Double, @ColorInt val color: Int)
}