package com.github.tehras.ui.playerdetails

import androidx.lifecycle.ViewModel
import com.github.tehras.base.arch.dagger.ViewModelFactoryModule
import com.github.tehras.base.arch.dagger.ViewModelKey
import com.github.tehras.base.dagger.components.SubComponentCreator
import com.github.tehras.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import javax.inject.Qualifier

@Module(includes = [ViewModelFactoryModule::class])
abstract class PlayerDetailsFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(PlayerDetailsViewModel::class)
    abstract fun bindPlayerDetailsViewModel(searchHistoryViewModel: PlayerDetailsViewModel): ViewModel
}

@FragmentScope
@Subcomponent(modules = [PlayerDetailsFragmentModule::class])
interface PlayerDetailsFragmentComponent {
    fun inject(fragment: PlayerDetailsFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun battleTag(@BattleTag battleTag: String): Builder

        fun build(): PlayerDetailsFragmentComponent
    }
}

interface PlayerDetailsFragmentComponentCreator : SubComponentCreator {
    fun plusPlayerDetailsFragmentComponentBuilder(): PlayerDetailsFragmentComponent.Builder
}

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class BattleTag
