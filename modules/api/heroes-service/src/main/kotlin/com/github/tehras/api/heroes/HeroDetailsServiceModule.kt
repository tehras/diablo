package com.github.tehras.api.heroes

import com.github.tehras.api.common.PrivateRetrofit
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object HeroDetailsServiceModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providePlayersRetrofit(@PrivateRetrofit retrofit: Retrofit): HeroDetailsService =
        retrofit.create(HeroDetailsService::class.java)
}