package com.github.tehras.ui.players.searchhistory.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.tehras.api.icons.heroIconLg
import com.github.tehras.base.ext.kotlin.format
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.Gender
import com.github.tehras.db.models.HeroeClass
import com.github.tehras.db.models.Player
import com.github.tehras.db.models.fromHero
import com.github.tehras.ui.commonviews.views.MultiColorBar
import com.github.tehras.ui.players.R
import com.github.tehras.ui.players.searchhistory.SearchHistoryUiEvent
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.players_search_history_list_item.*
import java.util.concurrent.TimeUnit

class SearchHistoryPlayersAdapter(private val playerConsumer: Consumer<SearchHistoryUiEvent>) :
    RecyclerView.Adapter<SearchHistoryPlayersViewHolder>(), Consumer<List<Player>> {
    private val players: MutableList<Player> = mutableListOf()
    private var disposable: Disposable? = null

    override fun accept(incomingPlayers: List<Player>) {
        disposable?.dispose()
        disposable = Single
            .fromCallable {
                DiffUtil
                    .calculateDiff(PlayerDiffUtil(incomingPlayers, players))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                players.clear()
                players.addAll(incomingPlayers)

                it.dispatchUpdatesTo(this)
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryPlayersViewHolder {
        return SearchHistoryPlayersViewHolder(
            parent.viewHolderFromParent(R.layout.players_search_history_list_item),
            playerConsumer
        )
    }

    override fun getItemCount(): Int = players.size
    override fun onBindViewHolder(holder: SearchHistoryPlayersViewHolder, position: Int) {
        holder.bind(player = players[position])
    }

}

class SearchHistoryPlayersViewHolder(
    override val containerView: View,
    private val playerClickConsumer: Consumer<SearchHistoryUiEvent>
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private var disposable: Disposable? = null
    private val cornerRadius by lazy {
        containerView.resources.getDimensionPixelSize(R.dimen.players_avatar_size).div(2)
    }

    @SuppressLint("SetTextI18n")
    fun bind(player: Player) {
        search_history_player_name.text = player.battleTag
        search_history_player_kills.text = "${player.kills.monsters.format()} | ${player.kills.elites.format()}"
        search_history_player_clan.text = if (player.guildName.isEmpty()) "None" else player.guildName
        search_history_paragon_level.text = "${player.paragonLevel}"

        search_history_player_avatar.populateAvatar(player)
        populateSections(player)

        disposable?.dispose()
        disposable = players_search_container
            .clicks()
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .map { SearchHistoryUiEvent.Selected(player.battleTag) }
            .subscribe(playerClickConsumer)
    }

    private fun ImageView.populateAvatar(player: Player) {
        var highestHero = 0.0
        var highestHeroeClass: HeroeClass? = null

        HeroeClass
            .values()
            .forEach {
                val value = player.timePlayed.fromHero(it)
                if (value > highestHero) {
                    highestHero = value
                    highestHeroeClass = it
                }
            }

        val icon = heroIconLg(highestHeroeClass?.iconName ?: "", Gender.random().toString().toLowerCase())

        GlideApp
            .with(context)
            .load(icon)
            .transforms(RoundedCorners(cornerRadius), CenterCrop())
            .into(this)
    }

    private fun populateSections(player: Player) {
        val sections =
            HeroeClass
                .values()
                .map { MultiColorBar.Section(player.timePlayed.fromHero(it), it.color) }

        search_history_player_bar.setSections(sections)
    }
}

private class PlayerDiffUtil(private val new: List<Player>, private val old: List<Player>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return new[newItemPosition].battleTag == old[oldItemPosition].battleTag
    }

    override fun getOldListSize(): Int = old.size
    override fun getNewListSize(): Int = new.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return new[newItemPosition] == old[oldItemPosition]
    }
}