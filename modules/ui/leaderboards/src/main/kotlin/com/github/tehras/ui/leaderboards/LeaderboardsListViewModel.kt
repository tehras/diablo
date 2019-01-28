/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.common.persist.SeasonPersistor
import com.github.tehras.api.icons.heroIconMd
import com.github.tehras.api.leaderboards.LeaderboardsPersistor
import com.github.tehras.api.leaderboards.LeaderboardsService
import com.github.tehras.api.leaderboards.LeaderboardsType
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet
import com.github.tehras.ui.leaderboards.adapter.LeaderboardsBody
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

/**
 * @author tkoshkin
 */
class LeaderboardsListViewModel @Inject constructor(
    private val tokenProvider: OauthTokenProvider,
    private val leaderboardsService: LeaderboardsService,
    private val dataConverter: LeaderboardsListDataConverter,
    private val leaderboardsPersistor: LeaderboardsPersistor,
    private val seasonPersistor: SeasonPersistor
) :
    ObservableViewModel<LeaderboardsListState, LeaderboardsListUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        val filterObservable = uiEvents()
            .ofType<LeaderboardsListUiEvent.LeaderboardsTypeSelected>()
            .map { it.type }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext {
                leaderboardsPersistor.updateLeaderboards(it)
            }
            .startWith(leaderboardsPersistor.currentLeaderboards())
            .shareBehavior()

        val leaderboardsSelectDialog = uiEvents()
            .ofType<LeaderboardsListUiEvent.LeaderboardsTypeFilterTapped>()
            .withLatestFrom(filterObservable)
            .map { (_, selected) ->
                LeaderboardsType
                    .values()
                    .map { mapTypeToItem(it, selected) }
            }
            .map { LeaderboardsDialog.LeaderboardsSelectDialog(it) }

        val tokenObservable = tokenProvider
            .oauthToken()
            .toObservable()

        val fetchData = Observables
            .combineLatest(tokenObservable, filterObservable)
            .flatMapSingle { (_, type) ->
                leaderboardsService.getLeaderboard(seasonPersistor.currentSeason(), type.serverType)
            }
            .subscribeOn(Schedulers.io())
            .map { dataConverter.convertToUiData(it) }
            .shareBehavior()

        val loadingStatus = Observable
            .merge(
                fetchData.map { LeaderboardsListState.DataState.SUCCESS },
                filterObservable.map { LeaderboardsListState.DataState.LOADING }
            )

        val dataRefresh = uiEvents()
            .ofType<LeaderboardsListUiEvent.LeaderboardsTypeSelected>()
            .map { listOf<LeaderboardsBody>() }

        val leaderboardsData = Observable
            .merge(dataRefresh, fetchData)

        val dialogsToShow = leaderboardsSelectDialog
            .flatMap { Observable.just(it, LeaderboardsDialog.NoDialog) }
            .startWith(LeaderboardsDialog.NoDialog)

        Observables
            .combineLatest(leaderboardsData, dialogsToShow, loadingStatus)
            .map { (data, dialog, status) ->
                LeaderboardsListState(
                    loadingState = status,
                    listData = data,
                    dialogToShow = dialog
                )
            }
            .startWith(LeaderboardsListState())
            .subscribeUntilDestroyed()
    }

    private fun mapTypeToItem(type: LeaderboardsType, selected: LeaderboardsType): SelectorBottomSheet.Item {
        val gender = {
            val rand = Random.nextInt(2)
            if (rand == 1) "male" else "female"
        }
        val imageUrl: String? = when (type) {
            LeaderboardsType.DUOS, LeaderboardsType.THREES, LeaderboardsType.QUADS -> null
            else -> heroIconMd(type.description.toLowerCase(), gender())
        }

        val icon: Int? = when (type) {
            LeaderboardsType.DUOS -> R.drawable.common_views_ic_duos
            LeaderboardsType.THREES -> R.drawable.common_views_ic_threes
            LeaderboardsType.QUADS -> R.drawable.common_views_ic_squads
            else -> null
        }

        return SelectorBottomSheet
            .Item(
                imageUrl = imageUrl,
                icon = icon,
                dataCode = type.ordinal,
                text = type.description,
                selected = selected == type
            )
    }
}