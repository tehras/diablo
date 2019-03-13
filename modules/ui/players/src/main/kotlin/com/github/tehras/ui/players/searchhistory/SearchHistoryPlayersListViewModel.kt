package com.github.tehras.ui.players.searchhistory

import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.Player
import io.reactivex.Scheduler
import javax.inject.Inject

class SearchHistoryPlayersListViewModel @Inject constructor(
    @DbExectutor private val dbExectutor: Scheduler,
    private val playersDao: PlayersDao
) : ObservableViewModel<SearchHistoryPlayersState, SearchHistoryPlayersUiEvent>() {
    override fun onCreate() {
        playersDao
            .getAll()
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

class SearchHistoryPlayersUiEvent