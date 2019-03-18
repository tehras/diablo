package com.github.tehras.ui.players.searchhistory.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.ui.players.R
import com.github.tehras.ui.players.searchhistory.SearchHistoryPlayersUiEvent
import io.reactivex.functions.Consumer
import kotlin.math.roundToInt


class SearchHistorySwipeHelper(private val handler: Consumer<SearchHistoryPlayersUiEvent>, context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val iconDelete by lazy { ContextCompat.getDrawable(context, R.drawable.players_ic_remove)!! }
    private val iconFavorite by lazy { ContextCompat.getDrawable(context, R.drawable.players_ic_favorite)!! }
    private val backgroundDelete by lazy { ContextCompat.getColor(context, R.color.players_remove).toDrawable() }
    private val backgroundFavorite by lazy { ContextCompat.getColor(context, R.color.players_favorite).toDrawable() }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> handler.accept(SearchHistoryPlayersUiEvent.RemoveFromRecent(position))
            ItemTouchHelper.RIGHT -> handler.accept(SearchHistoryPlayersUiEvent.FavoriteRecent(position))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c, recyclerView, viewHolder, dX,
            dY, actionState, isCurrentlyActive
        )

        val icon: Drawable
        val background: ColorDrawable
        if (dX > 0) {
            icon = iconFavorite
            background = backgroundFavorite
        } else {
            icon = iconDelete
            background = backgroundDelete
        }

        val itemView = viewHolder.itemView

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        var drawIcon = true

        when {
            dX > 0 -> { // Swiping to the right
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + icon.intrinsicWidth

                if (dX > iconLeft && dX < iconRight) {
                    val percentage = (dX - iconLeft) / (iconRight - iconLeft)
                    val heightOffset = ((iconTop - iconBottom).div(2)).times(1f - percentage).roundToInt()

                    icon.setBounds(
                        iconLeft, iconTop - heightOffset, dX.roundToInt(), iconBottom + heightOffset
                    )
                } else if (dX > iconRight) {
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                } else {
                    drawIcon = false
                }

                background.setBounds(
                    itemView.left, itemView.top,
                    (itemView.left + dX).roundToInt(),
                    itemView.bottom
                )
            }
            dX < 0 -> { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                val currentSwipe = (dX + itemView.right)

                if (currentSwipe > iconLeft && currentSwipe < iconRight) {
                    val percentage = (iconRight - currentSwipe) / (iconRight - iconLeft)
                    val heightOffset = ((iconTop - iconBottom).div(2)).times(1f - percentage).roundToInt()

                    icon.setBounds(
                        currentSwipe.roundToInt(), iconTop - heightOffset, iconRight, iconBottom + heightOffset
                    )
                } else if (currentSwipe < iconLeft) {
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                } else {
                    drawIcon = false
                }

                background.setBounds(currentSwipe.roundToInt(), itemView.top, itemView.right, itemView.bottom)
            }
            else -> // view is unSwiped
                drawIcon = false
        }

        background.draw(c)
        if (drawIcon) icon.draw(c)
        else {
            icon.setBounds(0, 0, 0, 0)
            icon.draw(c)
        }
    }
}