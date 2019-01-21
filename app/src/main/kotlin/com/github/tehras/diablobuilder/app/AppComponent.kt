package com.github.tehras.diablobuilder.app

import android.app.Application
import com.github.tehras.dagger.components.MainComponent
import com.github.tehras.dagger.modules.AppModule
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@Suppress("unused")
@ApplicationScope
@Component(
        modules = [
            AppModule::class
        ]
)
interface AppComponent :
        MainComponent {

    fun plusApplication(application: DiabloApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}