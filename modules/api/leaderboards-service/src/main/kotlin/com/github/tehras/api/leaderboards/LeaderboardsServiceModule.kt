/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.leaderboards

import com.github.tehras.api.common.OauthClient
import com.github.tehras.api.common.UrlResolver
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@Module
object LeaderboardsServiceModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesLeaderboardsService(@LeaderboardsRetrofit retrofit: Retrofit): LeaderboardsService =
        retrofit.create(LeaderboardsService::class.java)

    @Provides
    @JvmStatic
    @ApplicationScope
    @LeaderboardsRetrofit
    fun providesRetrofit(
        @OauthClient
        okHttpClient: OkHttpClient,
        rxJavaAdapter: RxJava2CallAdapterFactory,
        moshiAdapter: MoshiConverterFactory,
        urlResolver: UrlResolver
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlResolver.apiUrl())
            .addConverterFactory(moshiAdapter)
            .client(okHttpClient)
            .addCallAdapterFactory(rxJavaAdapter)
            .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LeaderboardsRetrofit