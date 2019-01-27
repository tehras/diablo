package com.github.tehras.ui.leaderboards.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.ui.leaderboards.R
import com.github.tehras.ui.leaderboards.adapter.LeaderboardsListAdapter.Type.*
import io.reactivex.functions.Consumer

class LeaderboardsListAdapter : RecyclerView.Adapter<LeaderboardsListBaseViewHolder>(),
    Consumer<List<LeaderboardsBody>> {

    private val data: MutableList<LeaderboardsBody> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardsListBaseViewHolder {
        val type = values()[viewType]

        return when (type) {
            SOLO -> LeaderboardsList1ViewHolder(parent.viewHolderFromParent(type.layout))
            DUOS -> LeaderboardsList2ViewHolder(parent.viewHolderFromParent(type.layout))
            THREES -> LeaderboardsList3ViewHolder(parent.viewHolderFromParent(type.layout))
            SQUADS -> LeaderboardsList4ViewHolder(parent.viewHolderFromParent(type.layout))
        }
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: LeaderboardsListBaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return data[position]
            .run {
                when {
                    players.size == 1 -> SOLO
                    players.size == 2 -> DUOS
                    players.size == 3 -> THREES
                    else -> SQUADS
                }
            }
            .ordinal
    }

    override fun accept(data: List<LeaderboardsBody>) {
        this.data.clear()
        this.data.addAll(data)

        notifyDataSetChanged()
    }

    enum class Type(val layout: Int) {
        SOLO(R.layout.leaderboards_list_1_player_layout),
        DUOS(R.layout.leaderboards_list_2_player_layout),
        THREES(R.layout.leaderboards_list_3_player_layout),
        SQUADS(R.layout.leaderboards_list_4_player_layout)
    }
}