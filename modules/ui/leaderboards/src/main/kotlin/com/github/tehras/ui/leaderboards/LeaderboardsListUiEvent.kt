/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import com.github.tehras.api.leaderboards.LeaderboardsType

/**
 * @author tkoshkin
 */
sealed class LeaderboardsListUiEvent {
    data class LeaderboardsTypeSelected(val type: LeaderboardsType) : LeaderboardsListUiEvent()
    object LeaderboardsTypeFilterTapped : LeaderboardsListUiEvent()
}