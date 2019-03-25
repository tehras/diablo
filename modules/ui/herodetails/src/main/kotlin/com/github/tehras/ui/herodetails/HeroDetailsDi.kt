package com.github.tehras.ui.herodetails

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
abstract class HeroDetailsFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(HeroDetailsViewModel::class)
    abstract fun bindHeroDetailsViewModel(searchHistoryViewModel: HeroDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HeroItemDetailsViewModel::class)
    abstract fun bindHeroItemDetailsViewModel(searchHistoryViewModel: HeroItemDetailsViewModel): ViewModel
}

@FragmentScope
@Subcomponent(modules = [HeroDetailsFragmentModule::class])
interface HomeDetailsFragmentComponent {
    fun inject(fragment: HeroDetailsFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun battleTag(@BattleTag battleTag: String): Builder

        @BindsInstance
        fun heroId(@HeroId heroId: Long): Builder

        fun build(): HomeDetailsFragmentComponent
    }
}

interface HomeDetailsFragmentComponentCreator : SubComponentCreator {
    fun plusHomeDetailssFragmentComponentBuilder(): HomeDetailsFragmentComponent.Builder
}

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class BattleTag

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class HeroId