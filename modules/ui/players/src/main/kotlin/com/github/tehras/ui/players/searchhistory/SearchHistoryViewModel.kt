package com.github.tehras.ui.players.searchhistory

import com.github.tehras.base.arch.ObservableViewModel
import javax.inject.Inject

class SearchHistoryViewModel @Inject constructor() : ObservableViewModel<SearchHistoryState, SearchHistoryUiEvent>() {

}

class SearchHistoryState
sealed class SearchHistoryUiEvent {
    data class Search(val text: String) : SearchHistoryUiEvent()
}