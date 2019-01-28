package com.github.tehras.ui.commonviews.bottomsheet

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.tehras.base.ext.fragment.withBundle
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.theme.resolveAttribute
import com.github.tehras.ui.commonviews.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.common_views_selector_bottom_sheet_item_layout.*
import kotlinx.android.synthetic.main.common_views_selector_bottom_sheet_layout.*

class SelectorBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val DATA_SELECTED_ITEM =
            "com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet.selected_item"
        private const val ARG_ITEMS = "com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet.items"
        private const val ARG_TITLE = "com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet.title"
        private const val ARG_REQUEST_CODE =
            "com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet.request_code"

        fun show(requestCode: Int, fragment: Fragment, title: String, vararg items: Item) {
            SelectorBottomSheet()
                .withBundle {
                    putParcelableArray(ARG_ITEMS, items)
                    putInt(ARG_REQUEST_CODE, requestCode)
                    putString(ARG_TITLE, title)
                }
                .also {
                    it.setTargetFragment(fragment, requestCode)
                    it.show(fragment.requireFragmentManager(), requestCode.toString())
                }
        }

        fun itemFromResult(intent: Intent): Item = intent.getParcelableExtra(DATA_SELECTED_ITEM)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.common_views_selector_bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selector_rv.run {
            setHasFixedSize(true)
            adapter = SelectorBottomSheetAdapter(items,
                Consumer {
                    itemSelected(it)
                    dismiss()
                })
            layoutManager = GridLayoutManager(context, 2)
        }

        selector_title.text = title
    }

    private fun itemSelected(item: Item) {
        targetFragment?.onActivityResult(requestCode, RESULT_OK, Intent()
            .apply {
                putExtra(DATA_SELECTED_ITEM, item)
            })
    }

    class SelectorBottomSheetAdapter(
        private val items: Array<Item>,
        private val selectConsumer: Consumer<Item>
    ) :
        RecyclerView.Adapter<SelectorBottomSheetAdapter.SelectorBottomSheetItem>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorBottomSheetItem {
            return SelectorBottomSheetItem(parent.viewHolderFromParent(R.layout.common_views_selector_bottom_sheet_item_layout))
        }

        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: SelectorBottomSheetItem, position: Int) {
            holder.bind(items[position])
        }

        inner class SelectorBottomSheetItem(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

            private val disposable = CompositeDisposable()

            fun bind(item: Item) {
                item.icon?.let {
                    selector_item_image.setImageDrawable(containerView.context.getDrawable(it))
                }
                val backgroundAttr = if (item.selected) {
                    R.attr.bgSecondary
                } else {
                    R.attr.selectableItemBackground
                }
                selector_item_container.background =
                        containerView.context.getDrawable(containerView.context.resolveAttribute(backgroundAttr))

                selector_item_container.isSelected = item.selected

                item.imageUrl?.let {
                    GlideApp
                        .with(selector_item_image)
                        .load(it)
                        .transforms(CircleCrop(), CenterCrop())
                        .into(selector_item_image)
                }

                selector_item_text.text = item.text
                disposable.clear()
                disposable += selector_item_container
                    .clicks()
                    .filter { !item.selected }
                    .map { item }
                    .subscribe(selectConsumer)
            }
        }
    }

    @Parcelize
    data class Item(
        @DrawableRes val icon: Int? = null, val imageUrl: String? = null,
        val text: String,
        val dataCode: Int,
        val selected: Boolean = false
    ) :
        Parcelable

    @Suppress("UNCHECKED_CAST")
    private val items: Array<SelectorBottomSheet.Item>
        get() = arguments!!.getParcelableArray(ARG_ITEMS) as Array<SelectorBottomSheet.Item>
    private val title: String
        get() = arguments!!.getString(ARG_TITLE)!!
    private val requestCode: Int
        get() = arguments!!.getInt(ARG_REQUEST_CODE)
}