package com.github.tehras.ui.playerdetails

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.players.PlayersService
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.base.arch.executors.NetworkExecutor
import com.github.tehras.base.arch.rx.GlobalBus
import com.github.tehras.base.arch.rx.NavEvent
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.base.arch.source.DataSource
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.Player
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import timber.log.Timber
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor(
    private val playersDao: PlayersDao,
    private val playersService: PlayersService,
    private val tokenProvider: OauthTokenProvider,
    private val bus: GlobalBus,
    @BattleTag private val battleTag: String,
    @DbExectutor private val dbExecutor: Scheduler,
    @NetworkExecutor private val networkExecutor: Scheduler
) :
    ObservableViewModel<PlayerDetailsState, PlayerDetailsUiEvent>() {

    private val createDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        val fromDb = playersDao
            .getBy(battleTag = battleTag)
            .map { PlayerData(it, DataSource.DB) }
            .subscribeOn(dbExecutor)
            .shareBehavior()

        val fromOnline = tokenProvider
            .oauthToken()
            .map { Timber.d("token incoming") }
            .flatMap { playersService.getPlayer(battleTag) }
            .map { PlayerData(it, DataSource.NETWORK) }
            .toObservable()
            .subscribeOn(networkExecutor)
            .shareBehavior()

        val playerObservable = Observable
            .merge<PlayerData>(fromDb, fromOnline)

        createDisposable += uiEvents()
            .ofType<PlayerDetailsUiEvent.ShowHero>()
            .withLatestFrom(playerObservable)
            .map { NavEvent.StartHeroDetailsScreen(it.second.player.battleTag, it.first.heroId) }
            .subscribe(bus)

        playerObservable
            .map { PlayerDetailsState(data = it) }
            .subscribeUntilDestroyed()
    }

    override fun onDestroy() {
        createDisposable.clear()

        super.onDestroy()
    }
}

data class PlayerDetailsState(val data: PlayerData)
data class PlayerData(val player: Player, val from: DataSource)

sealed class PlayerDetailsUiEvent {
    data class ShowHero(val heroId: Long) : PlayerDetailsUiEvent()
}