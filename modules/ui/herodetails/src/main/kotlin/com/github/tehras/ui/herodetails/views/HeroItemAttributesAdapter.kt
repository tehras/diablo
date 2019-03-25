package com.github.tehras.ui.herodetails.views

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.db.models.ItemAttribute
import com.github.tehras.ui.herodetails.R
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.herodetails_item_details_attributes_view.*
import kotlinx.android.synthetic.main.herodetails_item_details_title_view.*

class HeroItemAttributesAdapter : RecyclerView.Adapter<HeroItemAttributesViewHolder<*>>(),
    Consumer<List<UiItemDetails>> {
    private val itemDetails = mutableListOf<UiItemDetails>()
    override fun accept(details: List<UiItemDetails>) {
        val animate = itemDetails.size == 0

        itemDetails.clear()
        itemDetails.addAll(details)

        if (animate) {
            notifyItemRangeInserted(0, itemDetails.size)
        } else {
            notifyDataSetChanged()
        }
    }

    companion object {
        const val VIEW_TYPE_TITLE = 0
        const val VIEW_TYPE_DETAILS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroItemAttributesViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_TITLE -> HeroItemAttributesTitleViewHolder(parent.viewHolderFromParent(R.layout.herodetails_item_details_title_view))
            else -> HeroItemAttributesDetailsViewHolder(parent.viewHolderFromParent(R.layout.herodetails_item_details_attributes_view))
        }
    }

    override fun getItemCount(): Int = itemDetails.size
    override fun onBindViewHolder(holder: HeroItemAttributesViewHolder<*>, position: Int) {
        when (holder) {
            is HeroItemAttributesTitleViewHolder -> holder.bind((itemDetails[position] as UiItemDetails.Title).title)
            is HeroItemAttributesDetailsViewHolder -> holder.bind((itemDetails[position] as UiItemDetails.Details).itemDetails)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemDetails[position]) {
            is UiItemDetails.Title -> VIEW_TYPE_TITLE
            is UiItemDetails.Details -> VIEW_TYPE_DETAILS
        }
    }
}

sealed class UiItemDetails {
    data class Title(val title: String) : UiItemDetails()
    data class Details(val itemDetails: ItemAttribute) : UiItemDetails()
}

abstract class HeroItemAttributesViewHolder<T>(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    abstract fun bind(data: T)
}

class HeroItemAttributesDetailsViewHolder(containerView: View) :
    HeroItemAttributesViewHolder<ItemAttribute>(containerView) {
    override fun bind(data: ItemAttribute) {
        herodetails_item_attributes_details.text = Html.fromHtml(data.textHtml)
    }
}

class HeroItemAttributesTitleViewHolder(containerView: View) :
    HeroItemAttributesViewHolder<String>(containerView) {
    override fun bind(data: String) {
        herodetails_attributes_title.text = data
    }
}