package com.github.tehras.diablobuilder.app

import android.app.Application
import com.github.tehras.auth.AuthRetrofitModule
import com.github.tehras.dagger.components.components.MainComponent
import com.github.tehras.dagger.modules.AppModule
import com.github.tehras.dagger.scopes.ApplicationScope
import com.github.tehras.moshi.MoshiModule
import com.github.tehras.restapi.RetrofitModule
import dagger.BindsInstance
import dagger.Component

@Suppress("unused")
@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        MoshiModule::class,
        RetrofitModule::class,
        AuthRetrofitModule::class
    ]
)
interface AppComponent :
    MainComponent,
    UiComponents {

    fun plusApplication(application: DiabloApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}