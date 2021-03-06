package com.github.tehras.ui.players.searchhistory

import androidx.lifecycle.ViewModel
import com.github.tehras.base.arch.dagger.ViewModelFactoryModule
import com.github.tehras.base.arch.dagger.ViewModelKey
import com.github.tehras.base.dagger.components.SubComponentCreator
import com.github.tehras.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class SearchHistoryFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchHistorySearchViewModel::class)
    abstract fun bindSearchHistorySearchViewModel(searchHistoryViewModel: SearchHistorySearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchHistoryPlayersListViewModel::class)
    abstract fun bindSearchHistoryPlayersListViewModel(playersListViewModel: SearchHistoryPlayersListViewModel): ViewModel
}

@FragmentScope
@Subcomponent(modules = [SearchHistoryFragmentModule::class])
interface SearchHistoryFragmentComponent {
    fun inject(fragment: SearchHistoryFragment)
}

interface SearchHistoryFragmentComponentCreator : SubComponentCreator {
    fun plusSearchHistoryFragmentComponent(): SearchHistoryFragmentComponent
}