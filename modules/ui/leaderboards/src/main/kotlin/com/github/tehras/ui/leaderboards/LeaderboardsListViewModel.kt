/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.leaderboards.LeaderboardsPersistor
import com.github.tehras.api.leaderboards.LeaderboardsService
import com.github.tehras.base.arch.ObservableViewModel
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * @author tkoshkin
 */
class LeaderboardsListViewModel @Inject constructor(
    private val tokenProvider: OauthTokenProvider,
    private val leaderboardsService: LeaderboardsService,
    private val dataConverter: LeaderboardsListDataConverter,
    private val leaderboardsPersistor: LeaderboardsPersistor
) :
    ObservableViewModel<LeaderboardsListState, LeaderboardsListUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        val leaderboardsData = tokenProvider
            .oauthToken()
            .flatMap { leaderboardsService.getLeaderboard("16", "rift-team-4") }
            .subscribeOn(Schedulers.io())
            .map { dataConverter.convertToUiData(it, leaderboardsPersistor.currentLeaderboards()) }

        leaderboardsData
            .toObservable()
            .map {
                LeaderboardsListState(
                    loadingState = LeaderboardsListState.DataState.SUCCESS,
                    listData = it
                )
            }
            .doOnNext { Timber.d("new state :: $it") }
            .startWith(LeaderboardsListState())
            .subscribeUntilDestroyed()
    }
}