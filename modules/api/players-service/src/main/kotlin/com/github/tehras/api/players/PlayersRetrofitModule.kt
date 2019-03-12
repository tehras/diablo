package com.github.tehras.api.players

import com.github.tehras.api.auth.OauthClient
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
object PlayersRetrofitModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providePlayersRetrofit(@PlayersRetrofit retrofit: Retrofit): PlayersService =
        retrofit.create(PlayersService::class.java)

    @Provides
    @JvmStatic
    @ApplicationScope
    @PlayersRetrofit
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
annotation class PlayersRetrofit