package com.github.tehras.ui.players.searchhistory

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.players.PlayersService
import com.github.tehras.api.players.models.Player
import com.github.tehras.base.arch.ObservableViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SearchHistoryViewModel @Inject constructor(
    private val playersService: PlayersService,
    private val tokenProvider: OauthTokenProvider
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
                playersService.getPlayer(searchText).subscribeOn(Schedulers.io())
            }
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
}