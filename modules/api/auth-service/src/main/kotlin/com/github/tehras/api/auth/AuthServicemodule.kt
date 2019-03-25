package com.github.tehras.api.auth

import com.github.tehras.api.common.BasicAuthClient
import com.github.tehras.api.common.UrlResolver
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object AuthServicemodule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesRetrofit(
        @BasicAuthClient
        okHttpClient: OkHttpClient,
        rxJavaAdapter: RxJava2CallAdapterFactory,
        moshiAdapter: MoshiConverterFactory,
        urlResolver: UrlResolver
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlResolver.authUrl())
            .addConverterFactory(moshiAdapter)
            .client(okHttpClient)
            .addCallAdapterFactory(rxJavaAdapter)
            .build()
    }
}