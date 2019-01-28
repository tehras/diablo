package com.github.tehras.diablobuilder.app

import android.app.Application
import android.os.StrictMode
import com.github.tehras.base.dagger.components.ComponentProvider
import com.github.tehras.base.dagger.components.DaggerApplication
import com.github.tehras.base.log.CrashReportingTree
import com.github.tehras.diablobuilder.BuildConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import timber.log.Timber

/**
 * @author tkoshkin created on 8/24/18
 */
class DiabloApplication : Application(), DaggerApplication, ComponentProvider<AppComponent> {

    private lateinit var appComponent: AppComponent


    override fun getComponent(): AppComponent {
        return appComponent
    }

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        } else {
            Timber.plant(CrashReportingTree())

            AppCenter.start(
                this,
                "fcb64968-1337-4077-bd45-7362c113854f",
                Analytics::class.java, Crashes::class.java
            )
        }

        appComponent.plusApplication(this)

        super.onCreate()
    }
}