package com.github.tehras.ui.leaderboards.adapter

import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.ui.leaderboards.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.leaderboards_list_1_player_layout.*
import kotlinx.android.synthetic.main.leaderboards_list_2_player_layout.*
import kotlinx.android.synthetic.main.leaderboards_list_3_player_layout.*
import kotlinx.android.synthetic.main.leaderboards_list_4_player_layout.*
import kotlinx.android.synthetic.main.leaderboards_list_player_view.view.*
import kotlinx.android.synthetic.main.leaderboards_list_result_view.*

class LeaderboardsList4ViewHolder(override val containerView: View) : LeaderboardsListBaseViewHolder(containerView) {
    override fun bind(data: LeaderboardsBody) {
        bindResult(data)
        bindPlayer(data.players[0], leaderboards_overall_player_4_1)
        bindPlayer(data.players[1], leaderboards_overall_player_4_2)
        bindPlayer(data.players[2], leaderboards_overall_player_4_3)
        bindPlayer(data.players[3], leaderboards_overall_player_4_4)
    }
}

class LeaderboardsList3ViewHolder(override val containerView: View) : LeaderboardsListBaseViewHolder(containerView) {
    override fun bind(data: LeaderboardsBody) {
        bindResult(data)
        bindPlayer(data.players[0], leaderboards_overall_player_3_1)
        bindPlayer(data.players[1], leaderboards_overall_player_3_2)
        bindPlayer(data.players[2], leaderboards_overall_player_3_3)
    }
}

class LeaderboardsList2ViewHolder(override val containerView: View) : LeaderboardsListBaseViewHolder(containerView) {
    override fun bind(data: LeaderboardsBody) {
        bindResult(data)
        bindPlayer(data.players[0], leaderboards_overall_player_2_1)
        bindPlayer(data.players[1], leaderboards_overall_player_2_2)
    }
}

class LeaderboardsList1ViewHolder(override val containerView: View) : LeaderboardsListBaseViewHolder(containerView) {
    override fun bind(data: LeaderboardsBody) {
        bindResult(data)
        bindPlayer(data.players[0], leaderboards_overall_player_1_1)
    }
}

abstract class LeaderboardsListBaseViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    private val cornerRadius by lazy {
        containerView.resources.getDimensionPixelSize(R.dimen.leaderboards_avatar_size).div(2)
    }

    abstract fun bind(data: LeaderboardsBody)

    protected fun bindPlayer(player: LeaderboardsBody.LeaderboardsPlayer, playerLayout: View) {
        playerLayout.leaderboards_player_name.text = player.name
        playerLayout.leaderboards_player_hero_class.text = player.heroClass
        playerLayout.leaderboards_player_paragon.text =
                HtmlCompat.fromHtml("Paragon Level <b>${player.paragonLevel}</b>", HtmlCompat.FROM_HTML_MODE_COMPACT)

        GlideApp
            .with(containerView.context)
            .load(player.heroImageUrl)
            .transforms(RoundedCorners(cornerRadius), CenterCrop())
            .into(playerLayout.leaderboards_player_avatar)
    }

    protected fun bindResult(result: LeaderboardsBody) {
        leaderboards_result_rank_text.text = result.rank
        leaderboards_result_rift_level.text = result.riftLevel
        leaderboards_result_rift_time_completed.text = result.riftTime
        leaderboards_result_rift_completed_at.text = result.completedTime
    }
}

data class LeaderboardsBody(
    val rank: String,
    val riftLevel: String,
    val riftTime: String,
    val completedTime: String,
    val players: List<LeaderboardsPlayer>
) {

    data class LeaderboardsPlayer(
        val name: String,
        val heroClass: String,
        val heroImageUrl: String,
        val paragonLevel: String
    )
}