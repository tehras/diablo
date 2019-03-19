package com.github.tehras.ui.herodetails

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.api.heroes.HeroDetailsService
import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.arch.executors.DbExectutor
import com.github.tehras.base.arch.executors.NetworkExecutor
import com.github.tehras.db.dao.HeroesDao
import com.github.tehras.db.models.HeroDetails
import io.reactivex.Scheduler
import javax.inject.Inject

class HeroDetailsViewModel @Inject constructor(
    private val heroesDao: HeroesDao,
    private val heroDetailsService: HeroDetailsService,
    private val tokenProvider: OauthTokenProvider,
    @BattleTag private val battleTag: String,
    @HeroId private val heroId: Long,
    @DbExectutor private val dbExecutor: Scheduler,
    @NetworkExecutor private val networkExecutor: Scheduler
) : ObservableViewModel<HomeDetailsState, HomeDetailsUiEvent>() {
    override fun onCreate() {
        super.onCreate()

        val heroDetailsOnline = tokenProvider
            .oauthToken()
            .flatMap { heroDetailsService.getHeroDetails(battleTag, heroId.toString()) }
            .subscribeOn(networkExecutor)

        heroDetailsOnline
            .map(::HomeDetailsState)
            .toObservable()
            .subscribeUntilDestroyed()
    }
}

data class HomeDetailsState(val heroDetails: HeroDetails)
class HomeDetailsUiEvent