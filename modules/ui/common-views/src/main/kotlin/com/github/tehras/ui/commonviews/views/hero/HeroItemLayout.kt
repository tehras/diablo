package com.github.tehras.ui.commonviews.views.hero

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.ui.commonviews.R
import com.github.tehras.ui.commonviews.animations.fadeIn
import kotlinx.android.synthetic.main.common_views_hero_item_layout.view.*

class HeroItemLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.common_views_hero_item_layout, this)
    }

    fun populate(color: String, itemUrl: String) {
        assignColorToBackground(HeroItemColor.toColor(color))
        if (itemUrl.isNotBlank()) assignImageView(itemUrl)
    }

    private fun assignImageView(itemUrl: String) {
        GlideApp
            .with(context)
            .load(itemUrl)
            .fitCenter()
            .encodeQuality(20)
            .into(common_views_hero_item_image)
    }

    @Suppress("DEPRECATION")
    private fun assignColorToBackground(color: HeroItemColor) {
        common_views_hero_item_container.isVisible = true
        common_views_hero_item_container.fadeIn()
        ContextThemeWrapper(
            context,
            R.style.Theme_AppCompat_DayNight
        ).getDrawable(R.drawable.common_views_hero_item_background)
            ?.also {
                it.setColorFilter(context.resources.getColor(color.color), PorterDuff.Mode.SRC_ATOP)

                common_views_hero_item_container.background = it
            }
    }
}