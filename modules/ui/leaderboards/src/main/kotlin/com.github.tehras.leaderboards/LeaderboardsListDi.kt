/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.leaderboards

import androidx.lifecycle.ViewModel
import com.github.tehras.arch.dagger.ViewModelFactoryModule
import com.github.tehras.arch.dagger.ViewModelKey
import com.github.tehras.dagger.components.components.SubComponentCreator
import com.github.tehras.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

/**
 * @author tkoshkin created on 8/24/18
 */
@Module(includes = [ViewModelFactoryModule::class])
abstract class LeadervoardsListModule {
    @Binds
    @IntoMap
    @ViewModelKey(LeaderboardsListViewModel::class)
    abstract fun bindLeaderboardsListViewModel(leaderboardsListViewModel: LeaderboardsListViewModel): ViewModel
}

@FragmentScope
@Subcomponent(modules = [LeadervoardsListModule::class])
interface LeaderboardsListComponent {
    fun inject(fragment: LeaderboardsListFragment)
}

interface LeaderboardsListComponentCeator : SubComponentCreator {
    fun plusLeaderboardsListComponent(): LeaderboardsListComponent
}