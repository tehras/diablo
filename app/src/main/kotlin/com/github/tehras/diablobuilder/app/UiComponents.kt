/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.diablobuilder.app

import com.github.tehras.ui.herodetails.HomeDetailsFragmentComponentCreator
import com.github.tehras.ui.leaderboards.LeaderboardsListComponentCreator
import com.github.tehras.ui.playerdetails.PlayerDetailsFragmentComponentCreator
import com.github.tehras.ui.players.searchhistory.SearchHistoryFragmentComponentCreator

/**
 * @author tkoshkin
 */
interface UiComponents : LeaderboardsListComponentCreator,
    SearchHistoryFragmentComponentCreator,
    PlayerDetailsFragmentComponentCreator,
    HomeDetailsFragmentComponentCreator