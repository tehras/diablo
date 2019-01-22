package com.github.tehras.diablobuilder.app

import android.app.Application
import android.content.SharedPreferences
import android.os.StrictMode
import com.github.tehras.dagger.components.components.ComponentProvider
import com.github.tehras.dagger.components.components.DaggerApplication
import com.github.tehras.diablobuilder.BuildConfig
import com.github.tehras.log.CrashReportingTree
import timber.log.Timber
import javax.inject.Inject

/**
 * @author tkoshkin created on 8/24/18
 */
class DiabloApplication : Application(), DaggerApplication, ComponentProvider<AppComponent> {

    private lateinit var appComponent: AppComponent


    override fun getComponent(): AppComponent {
        return appComponent
    }

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            )

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
        }

        appComponent.plusApplication(this)

        super.onCreate()
    }
}