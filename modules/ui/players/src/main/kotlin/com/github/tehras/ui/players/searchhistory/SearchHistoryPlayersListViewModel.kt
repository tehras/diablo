package com.github.tehras.ui.players.searchhistory

import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.Player
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchHistoryPlayersListViewModel @Inject constructor(
    @DbExectutor private val dbExectutor: Scheduler,
    private val playersDao: PlayersDao
) : ObservableViewModel<SearchHistoryPlayersState, SearchHistoryPlayersUiEvent>() {
    override fun onCreate() {
        val playersObservable = playersDao
            .getAll()
            .shareBehavior()

        uiEvents()
            .ofType<SearchHistoryPlayersUiEvent.RemoveFromRecent>()
            .withLatestFrom(playersObservable)
            .map { (remove, players) ->
                players[remove.position]
            }
            .flatMapCompletable {
                playersDao
                    .delete(it)
                    .subscribeOn(dbExectutor)
            }
            .subscribe()

        playersObservable
            .subscribeOn(dbExectutor)
            .map { SearchHistoryPlayersState(players = it, isLoading = false) }
            .startWith(SearchHistoryPlayersState(listOf(), true))
            .subscribeUntilDestroyed()
    }
}

data class SearchHistoryPlayersState(
    val players: List<Player>,
    val isLoading: Boolean
)

sealed class SearchHistoryPlayersUiEvent {
    data class RemoveFromRecent(val position: Int) : SearchHistoryPlayersUiEvent()
    data class FavoriteRecent(val position: Int) : SearchHistoryPlayersUiEvent()
}