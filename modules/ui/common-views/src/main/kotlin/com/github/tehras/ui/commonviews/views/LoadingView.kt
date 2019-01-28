package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.github.tehras.ui.commonviews.R
import kotlinx.android.synthetic.main.common_views_loading_view.view.*

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.common_views_loading_view, this)

        hide()
        elevation = (context.resources.getDimensionPixelSize(R.dimen.elevation_large) * 2).toFloat()
    }

    fun show() {
        common_views_lottie_anim.playAnimation()
        visibility = View.VISIBLE
    }

    fun hide() {
        common_views_lottie_anim.cancelAnimation()
        visibility = View.GONE
    }
}