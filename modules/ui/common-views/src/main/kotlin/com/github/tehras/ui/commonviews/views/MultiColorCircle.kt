package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet


class MultiColorCircle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MultiColorLayout(context, attrs, defStyleAttr) {
    private val wheelWidth =
        context.resources.getDimensionPixelSize(com.github.tehras.ui.commonviews.R.dimen.common_views_color_wheel_width)
            .toFloat()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = wheelWidth
    }
    private val oval: RectF by lazy {
        val centerX = measuredWidth / 2
        val centerY = measuredHeight / 2
        val radius = Math.min(centerX, centerY)
        val startTop = (wheelWidth / 2).toInt()

        val endBottom = 2 * radius - startTop
        RectF(startTop.toFloat(), startTop.toFloat(), endBottom.toFloat(), endBottom.toFloat())
    }

    override fun drawColors(canvas: Canvas) {
        if (sections.isEmpty()) return

        var arcStartingAngle = 0f
        sections.filter { it.percentage != 0.0 }.forEach {
            val sweep = it.percentage.toFloat().times(360f)
            paint.color = it.color
            canvas.drawArc(oval, arcStartingAngle, sweep, false, paint)

            arcStartingAngle += sweep
        }
    }
}