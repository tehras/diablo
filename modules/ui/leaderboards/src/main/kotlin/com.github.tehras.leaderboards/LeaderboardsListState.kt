/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.leaderboards

/**
 * @author tkoshkin
 */
data class LeaderboardsListState(
    val loadingState: DataState = DataState.LOADING
) {
    enum class DataState {
        LOADING, ERROR, SUCCESS
    }
}