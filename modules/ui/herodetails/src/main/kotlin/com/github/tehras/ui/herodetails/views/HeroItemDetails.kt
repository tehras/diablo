package com.github.tehras.ui.herodetails.views

import android.text.Html
import android.view.View
import com.github.tehras.api.icons.itemIconSm
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.Item
import com.github.tehras.ui.herodetails.R
import com.klinker.android.peekview.PeekViewActivity
import com.klinker.android.peekview.builder.Peek
import com.klinker.android.peekview.builder.PeekViewOptions
import com.klinker.android.peekview.callback.SimpleOnPeek
import kotlinx.android.synthetic.main.herodetails_item_peek_details.view.*

class HeroItemDetails(activity: PeekViewActivity, view: View, private val item: Item) : SimpleOnPeek() {
    init {

        Peek.into(R.layout.herodetails_item_peek_details, this)
            .with(PeekViewOptions().apply {
                setBlurBackground(false)
                hapticFeedback = true
                heightPercent = 0.4f
            })
            .applyTo(activity, view)
    }

    override fun onInflated(root: View) {
        root.herodetails_peek_title.text = item.name
        @Suppress("DEPRECATION")
        root.herodetails_peek_description.text = Html.fromHtml(item.tooltipParams)

        GlideApp
            .with(root.context)
            .load(itemIconSm(item.icon))
            .fitCenter()
            .encodeQuality(20)
            .into(root.herodetails_peek_icon)
    }
}