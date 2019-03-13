package com.github.tehras.ui.players.searchhistory

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.players.PlayersService
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.base.arch.executors.NetworkExecutor
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.Player
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.withLatestFrom
import timber.log.Timber
import javax.inject.Inject

class SearchHistorySearchViewModel @Inject constructor(
    private val playersService: PlayersService,
    private val tokenProvider: OauthTokenProvider,
    @NetworkExecutor private val networkScheduler: Scheduler,
    @DbExectutor private val dbScheduler: Scheduler,
    private val playersDao: PlayersDao
) : ObservableViewModel<SearchHistoryState, SearchHistoryUiEvent>() {
    override fun onCreate() {
        val tokenObservable = tokenProvider
            .oauthToken()
            .toObservable()

        val searchField = uiEvents()
            .ofType<SearchHistoryUiEvent.Search>()
            .map { it.text }

        val searchResultObservable: Observable<SearchResult> = searchField
            .withLatestFrom(tokenObservable)
            .map { it.first }
            .switchMapSingle { searchText ->
                playersService
                    .getPlayer(searchText)
                    .subscribeOn(networkScheduler)
            }
            .doOnNext(::savePlayer)
            .map { SearchResult.Success(it) as SearchResult }
            .startWith(SearchResult.NoResult)
            .onErrorReturn {
                Timber.e(it)
                SearchResult.Error
            }

        searchResultObservable
            .map { SearchHistoryState(it) }
            .subscribeUntilDestroyed()
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
    data class Success(val player: com.github.tehras.db.models.Player) : SearchResult()
}

sealed class SearchHistoryUiEvent {
    data class Search(val text: String) : SearchHistoryUiEvent()
}