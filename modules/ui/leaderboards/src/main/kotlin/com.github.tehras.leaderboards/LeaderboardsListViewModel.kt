/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.leaderboards

import com.github.tehras.arch.ObservableViewModel
import com.github.tehras.auth.OauthTokenProvider
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * @author tkoshkin
 */
class LeaderboardsListViewModel @Inject constructor(private val tokenProvider: OauthTokenProvider) :
    ObservableViewModel<LeaderboardsListState, LeaderboardsListUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        tokenProvider
            .oauthToken()
            .subscribeOn(Schedulers.io())
            .subscribeBy {
                Timber.d("response :: $it")
            }
    }
}