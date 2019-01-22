/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.leaderboards.LeaderboardsService
import com.github.tehras.base.arch.ObservableViewModel
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * @author tkoshkin
 */
class LeaderboardsListViewModel @Inject constructor(
    private val tokenProvider: OauthTokenProvider,
    private val leaderboardsService: LeaderboardsService
) :
    ObservableViewModel<LeaderboardsListState, LeaderboardsListUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        tokenProvider
            .oauthToken()
            .flatMap { leaderboardsService.getLeaderboard("16", "achievement-points") }
            .subscribeOn(Schedulers.io())
            .subscribeBy {
                Timber.d("response :: $it")
            }
    }
}