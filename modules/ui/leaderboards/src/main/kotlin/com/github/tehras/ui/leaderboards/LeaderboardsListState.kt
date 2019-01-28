/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet
import com.github.tehras.ui.leaderboards.adapter.LeaderboardsBody

/**
 * @author tkoshkin
 */
data class LeaderboardsListState(
    val loadingState: DataState = DataState.LOADING,
    val listData: List<LeaderboardsBody> = listOf(),
    val dialogToShow: LeaderboardsDialog = LeaderboardsDialog.NoDialog
) {
    enum class DataState {
        LOADING, ERROR, SUCCESS
    }
}

sealed class LeaderboardsDialog {
    object NoDialog : LeaderboardsDialog()
    data class LeaderboardsSelectDialog(val items: List<SelectorBottomSheet.Item>) : LeaderboardsDialog()
}