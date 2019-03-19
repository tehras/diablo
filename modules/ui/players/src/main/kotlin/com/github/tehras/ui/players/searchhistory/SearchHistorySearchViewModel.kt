package com.github.tehras.ui.players.searchhistory

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.players.PlayersService
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.base.arch.executors.NetworkExecutor
import com.github.tehras.base.arch.rx.GlobalBus
import com.github.tehras.base.arch.rx.NavEvent
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.Player
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

class SearchHistorySearchViewModel @Inject constructor(
    private val playersService: PlayersService,
    private val tokenProvider: OauthTokenProvider,
    @NetworkExecutor private val networkScheduler: Scheduler,
    @DbExectutor private val dbScheduler: Scheduler,
    private val playersDao: PlayersDao,
    private val globalBus: GlobalBus
) : ObservableViewModel<SearchHistoryState, SearchHistoryUiEvent>() {
    private val createDisposable = CompositeDisposable()

    override fun onCreate() {
        val tokenObservable = tokenProvider
            .oauthToken()
            .toObservable()

        val searchField = uiEvents()
            .ofType<SearchHistoryUiEvent.Search>()
            .map { it.text }

        val playerSelected = uiEvents()
            .ofType<SearchHistoryUiEvent.Selected>()
            .map { it.battleTag }

        val searchResultObservable: Observable<SearchResult> = searchField
            .withLatestFrom(tokenObservable)
            .map { it.first }
            .switchMapSingle { searchText ->
                playersService
                    .getPlayer(searchText)
                    .subscribeOn(networkScheduler)
                    .doOnSuccess(::savePlayer)
                    .map { SearchResult.Success(it) as SearchResult }
                    .onErrorReturn {
                        SearchResult.Error
                    }
            }
            .startWith(SearchResult.NoResult)
            .shareBehavior()

        searchResultObservable
            .map { SearchHistoryState(it) }
            .subscribeOn(networkScheduler)
            .subscribeUntilDestroyed()

        val searchSuccessResult = searchResultObservable
            .ofType<SearchResult.Success>()
            .map { it.player.battleTag }

        createDisposable += Observable
            .merge(searchSuccessResult, playerSelected)
            .map(NavEvent::StartPlayerDetailsScreen)
            .subscribe(globalBus)
    }

    override fun onDestroy() {
        createDisposable.clear()

        super.onDestroy()
    }

    private fun savePlayer(player: Player) {
        playersDao
            .insert(player)
            .subscribeOn(dbScheduler)
            .subscribe()
    }
}

data class SearchHistoryState(
    val result: SearchResult
)

sealed class SearchResult {
    object NoResult : SearchResult()
    object Error : SearchResult()
    data class Success(val player: Player) : SearchResult()
}

sealed class SearchHistoryUiEvent {
    data class Search(val text: String) : SearchHistoryUiEvent()
    data class Selected(val battleTag: String) : SearchHistoryUiEvent()
}