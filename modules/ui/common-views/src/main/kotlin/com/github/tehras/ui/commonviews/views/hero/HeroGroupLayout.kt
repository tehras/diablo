package com.github.tehras.ui.commonviews.views.hero

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.tehras.ui.commonviews.R
import kotlinx.android.synthetic.main.common_views_hero_group_layout.view.*

class HeroGroupLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.common_views_hero_group_layout, this)
    }

    fun head(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_head.populate(color, itemUrl)
        return common_views_hero_item_head
    }

    fun shoulders(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_shoulder.populate(color, itemUrl)
        return common_views_hero_item_shoulder
    }

    fun bracers(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_bracers.populate(color, itemUrl)
        return common_views_hero_item_bracers
    }

    fun leftRing(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_l_ring.populate(color, itemUrl)
        return common_views_hero_item_l_ring
    }

    fun rightRing(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_r_ring.populate(color, itemUrl)
        return common_views_hero_item_r_ring
    }

    fun legs(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_legs.populate(color, itemUrl)
        return common_views_hero_item_legs
    }

    fun boots(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_boots.populate(color, itemUrl)
        return common_views_hero_item_boots
    }

    fun body(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_body.populate(color, itemUrl)
        return common_views_hero_item_body
    }

    fun arms(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_arms.populate(color, itemUrl)
        return common_views_hero_item_arms
    }

    fun primaryWeapon(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_weapon_primary.populate(color, itemUrl)
        return common_views_hero_item_weapon_primary
    }

    fun secondaryWeapon(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_weapon_secondary.populate(color, itemUrl)
        return common_views_hero_item_weapon_secondary
    }

    fun belt(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_belt.populate(color, itemUrl)
        return common_views_hero_item_belt
    }

    fun neck(color: String, itemUrl: String): HeroItemLayout {
        common_views_hero_item_necklace.populate(color, itemUrl)
        return common_views_hero_item_necklace
    }
}