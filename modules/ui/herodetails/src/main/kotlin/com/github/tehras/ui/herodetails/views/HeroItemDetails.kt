package com.github.tehras.ui.herodetails.views

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.api.icons.itemIconSm
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.Item
import com.github.tehras.ui.herodetails.HeroItemDetailsUiEvent
import com.github.tehras.ui.herodetails.HeroItemDetailsViewModel
import com.github.tehras.ui.herodetails.R
import com.klinker.android.peekview.PeekViewActivity
import com.klinker.android.peekview.builder.Peek
import com.klinker.android.peekview.builder.PeekViewOptions
import com.klinker.android.peekview.callback.SimpleOnPeek
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.android.synthetic.main.herodetails_item_peek_details.view.*

class HeroItemDetails(
    activity: PeekViewActivity,
    view: View,
    private val item: Item,
    provider: HeroItemDetailsViewModelProvider
) : SimpleOnPeek() {
    private val viewModel = provider.itemDetailsViewModel()

    init {
        Peek.into(R.layout.herodetails_item_peek_details, this)
            .with(PeekViewOptions().apply {
                setBlurBackground(false)
                hapticFeedback = true
                widthPercent = 0.8f
            })
            .applyTo(activity, view)
    }

    private val adapter by lazy { HeroItemAttributesAdapter() }
    private val subs = CompositeDisposable()

    override fun onInflated(root: View) {
        viewModel.accept(HeroItemDetailsUiEvent.LoadItem(item))

        root.herodetails_peek_title.text = item.name
        initAttributes(root.herodetails_peek_attributes)

        GlideApp
            .with(root.context)
            .load(itemIconSm(item.icon))
            .fitCenter()
            .encodeQuality(20)
            .into(root.herodetails_peek_icon)

        val state = viewModel
            .observeState()
            .filter { it.itemId == item.id }
            .observeOn(AndroidSchedulers.mainThread())
//        subs += state
//            .map { it.heroItemDetails }
//            .subscribe(::populateUi)

        subs += state
            .map { it.heroAttributes }
            .subscribe(adapter)
    }

    private fun initAttributes(recycler: RecyclerView) {
        recycler.run {
            adapter = this@HeroItemDetails.adapter
            itemAnimator = SlideInDownAnimator()
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun dismissed() {
        subs.clear()

        super.dismissed()
    }
}

interface HeroItemDetailsViewModelProvider {
    fun itemDetailsViewModel(): HeroItemDetailsViewModel
}