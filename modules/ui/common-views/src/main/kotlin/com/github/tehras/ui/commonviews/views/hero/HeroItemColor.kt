package com.github.tehras.ui.commonviews.views.hero

import androidx.annotation.ColorRes
import com.github.tehras.ui.commonviews.R

enum class HeroItemColor(@ColorRes val color: Int) {
    COMMON(R.color.common_views_hero_common),
    BLUE(R.color.common_views_hero_blue),
    YELLOW(R.color.common_views_hero_yellow),
    ORANGE(R.color.common_views_hero_orange),
    GREEN(R.color.common_views_hero_green);

    companion object {
        fun toColor(color: String): HeroItemColor =
            values().firstOrNull { itemColor -> itemColor.name.toLowerCase() == color } ?: COMMON
    }
}