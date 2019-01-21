package com.github.tehras.moshi

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**  * @author tkoshkin created on 8/26/18  */
@Module
class MoshiModule {
    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun providesMoshi(): Moshi = Moshi.Builder().build()
    }
}