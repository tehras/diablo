package com.github.tehras.ui.herodetails.delegates

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.github.tehras.api.icons.itemIconLg
import com.github.tehras.db.models.Item
import com.github.tehras.ui.commonviews.views.hero.HeroGroupLayout
import com.github.tehras.ui.commonviews.views.hero.HeroItemColor
import com.github.tehras.ui.herodetails.views.HeroItemDetails
import com.github.tehras.ui.herodetails.views.HeroItemDetailsViewModelProvider
import com.klinker.android.peekview.PeekViewActivity

class HeroItemsDelegate private constructor() : LifecycleObserver {

    companion object Factory {
        fun attach(fragment: Fragment) = HeroItemsDelegate().also {
            fragment.lifecycle.addObserver(it)
        }
    }

    fun populateHeroDetails(items: Map<String, Item>, heroLayout: HeroGroupLayout, fragment: Fragment) {
        heroLayout.run {
            fromItems(items, "head").fill(fragment) { head(first, second) }
            fromItems(items, "neck").fill(fragment) { neck(first, second) }
            fromItems(items, "torso").fill(fragment) { body(first, second) }
            fromItems(items, "shoulders").fill(fragment) { shoulders(first, second) }
            fromItems(items, "legs").fill(fragment) { legs(first, second) }
            fromItems(items, "waist").fill(fragment) { belt(first, second) }
            fromItems(items, "hands").fill(fragment) { arms(first, second) }
            fromItems(items, "bracers").fill(fragment) { bracers(first, second) }
            fromItems(items, "feet").fill(fragment) { boots(first, second) }
            fromItems(items, "leftFinger").fill(fragment) { leftRing(first, second) }
            fromItems(items, "rightFinger").fill(fragment) { rightRing(first, second) }
            fromItems(items, "mainHand").fill(fragment) { primaryWeapon(first, second) }
            fromItems(items, "offHand").fill(fragment) { secondaryWeapon(first, second) }
        }
    }

    private fun fromItems(items: Map<String, Item>, name: String): Item? {
        return items[name]
    }

    private fun convertToPair(item: Item?): Pair<String, String> {
        return if (item == null) {
            Pair(HeroItemColor.COMMON.name.toLowerCase(), "")
        } else {
            Pair(item.displayColor, itemIconLg(item.icon))
        }
    }

    private fun Item?.fill(fragment: Fragment, block: Pair<String, String>.() -> View) {
        convertToPair(this).block().apply {
            if (this@fill != null) HeroItemDetails(
                fragment.activity as PeekViewActivity,
                this,
                this@fill,
                fragment as HeroItemDetailsViewModelProvider
            )
        }
    }
}