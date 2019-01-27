/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import com.github.tehras.ui.leaderboards.adapter.LeaderboardsBody

/**
 * @author tkoshkin
 */
data class LeaderboardsListState(
    val loadingState: DataState = DataState.LOADING,
    val listData: List<LeaderboardsBody> = listOf()
) {
    enum class DataState {
        LOADING, ERROR, SUCCESS
    }
}