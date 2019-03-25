package com.github.tehras.diablobuilder.app

import android.app.Application
import com.github.tehras.api.auth.AuthServicemodule
import com.github.tehras.api.auth.ClientModule
import com.github.tehras.api.common.PrivateDiabloRetrofitModule
import com.github.tehras.api.heroes.HeroDetailsServiceModule
import com.github.tehras.api.items.ItemsServiceModule
import com.github.tehras.api.leaderboards.LeaderboardsServiceModule
import com.github.tehras.api.players.PlayersServiceModule
import com.github.tehras.base.arch.executors.ExecutorsModule
import com.github.tehras.base.arch.rx.GlobalBusModule
import com.github.tehras.base.dagger.components.MainComponent
import com.github.tehras.base.moshi.MoshiModule
import com.github.tehras.base.restapi.RetrofitModule
import com.github.tehras.dagger.modules.AppModule
import com.github.tehras.dagger.scopes.ApplicationScope
import com.github.tehras.db.DatabaseModule
import dagger.BindsInstance
import dagger.Component

@Suppress("unused")
@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        MoshiModule::class,
        ExecutorsModule::class,
        ClientModule::class,
        RetrofitModule::class,
        AuthServicemodule::class,
        LeaderboardsServiceModule::class,
        ItemsServiceModule::class,
        PlayersServiceModule::class,
        HeroDetailsServiceModule::class,
        DatabaseModule::class,
        GlobalBusModule::class,
        PrivateDiabloRetrofitModule::class
    ]
)
interface AppComponent :
    MainComponent,
    UiComponents,
    Injectors {

    fun plusApplication(application: DiabloApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}