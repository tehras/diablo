package com.github.tehras.ui.players.searchhistory.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.db.models.Player
import com.github.tehras.ui.players.R
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.players_search_history_list_item.*

class SearchHistoryPlayersAdapter : RecyclerView.Adapter<SearchHistoryPlayersViewHolder>(), Consumer<List<Player>> {
    private val players: MutableList<Player> = mutableListOf()

    override fun accept(incomingPlayers: List<Player>) {
        // TODO use DiffUtil
        players.clear()
        players.addAll(incomingPlayers)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryPlayersViewHolder {
        return SearchHistoryPlayersViewHolder(parent.viewHolderFromParent(R.layout.players_search_history_list_item))
    }

    override fun getItemCount(): Int = players.size
    override fun onBindViewHolder(holder: SearchHistoryPlayersViewHolder, position: Int) {
        holder.bind(player = players[position])
    }

}

class SearchHistoryPlayersViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(player: Player) {
        search_history_player_name.text = player.battleTag
    }
}