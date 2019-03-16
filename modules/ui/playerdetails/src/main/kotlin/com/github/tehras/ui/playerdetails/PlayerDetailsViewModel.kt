package com.github.tehras.ui.playerdetails

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.players.PlayersService
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.base.arch.executors.NetworkExecutor
import com.github.tehras.base.arch.source.DataSource
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.Player
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor(
    private val playersDao: PlayersDao,
    private val playersService: PlayersService,
    private val tokenProvider: OauthTokenProvider,
    @BattleTag private val battleTag: String,
    @DbExectutor private val dbExecutor: Scheduler,
    @NetworkExecutor private val networkExecutor: Scheduler
) :
    ObservableViewModel<PlayerDetailsState, PlayerDetailsUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        val tokenObservable = tokenProvider
            .oauthToken()

        val fromDb = playersDao
            .getBy(battleTag = battleTag)
            .map { PlayerData(it, DataSource.DB) }
            .subscribeOn(dbExecutor)

        val fromOnline = tokenObservable
            .flatMap { playersService.getPlayer(battleTag) }
            .map { PlayerData(it, DataSource.NETWORK) }
            .toObservable()
            .subscribeOn(networkExecutor)

        val playerObservable = Observable
            .merge<PlayerData>(fromDb, fromOnline)

        playerObservable
            .map { PlayerDetailsState(data = it) }
            .subscribeUntilDestroyed()
    }

}

data class PlayerDetailsState(val data: PlayerData)
data class PlayerData(val player: Player, val from: DataSource)
class PlayerDetailsUiEvent