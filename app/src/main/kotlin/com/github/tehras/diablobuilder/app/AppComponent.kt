package com.github.tehras.diablobuilder.app

import android.app.Application
import com.github.tehras.api.auth.AuthRetrofitModule
import com.github.tehras.api.auth.ClientModule
import com.github.tehras.api.leaderboards.LeaderboardsRetrofitModule
import com.github.tehras.api.players.PlayersRetrofitModule
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
        AuthRetrofitModule::class,
        LeaderboardsRetrofitModule::class,
        PlayersRetrofitModule::class,
        DatabaseModule::class,
        GlobalBusModule::class
    ]
)
interface AppComponent :
    MainComponent,
    UiComponents,
    Injectors
{

    fun plusApplication(application: DiabloApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}