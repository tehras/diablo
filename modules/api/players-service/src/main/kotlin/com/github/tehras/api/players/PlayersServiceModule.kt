package com.github.tehras.api.players

import com.github.tehras.api.common.PrivateRetrofit
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
object PlayersServiceModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providePlayersRetrofit(@PrivateRetrofit retrofit: Retrofit): PlayersService =
        retrofit.create(PlayersService::class.java)
}
