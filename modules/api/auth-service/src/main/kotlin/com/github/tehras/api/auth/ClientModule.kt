/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.auth

import com.github.tehras.api.auth.interceptors.BasicAuthInterceptor
import com.github.tehras.api.auth.interceptors.OauthInterceptor
import com.github.tehras.api.auth.oauth.OauthTokenProvider
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Qualifier

/**
 * @author tkoshkin
 */
@Module
object ClientModule {
    @OauthClient
    @JvmStatic
    @Provides
    fun providesOauthClient(
        @DefaultClient okHttpClient: OkHttpClient,
        oauthTokenProvider: OauthTokenProvider
    ): OkHttpClient {
        return okHttpClient
            .newBuilder()
            .logginInterceptor()
            .addInterceptor(OauthInterceptor(oauthTokenProvider))
            .build()
    }

    @BasicAuthClient
    @JvmStatic
    @Provides
    fun providesBasicAuthClient(
        @DefaultClient okHttpClient: OkHttpClient
    ): OkHttpClient {
        return okHttpClient
            .newBuilder()
            .addInterceptor(BasicAuthInterceptor())
            .build()
    }

    @Provides
    @JvmStatic
    @DefaultClient
    @ApplicationScope
    fun providesOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    private fun OkHttpClient.Builder.logginInterceptor(): OkHttpClient.Builder {
        this.addInterceptor {
            // This is only present for Debugging Purposes
            val newRequest = it.request()
                .newBuilder()
                .build()

            Timber.d("Request :: url :: ${newRequest.url()}")
            Timber.d("Request :: url :: ${newRequest.headers()}")

            val response = it.proceed(newRequest)
            Timber.d("Response :: success :: ${response.isSuccessful}")
            Timber.d("Response :: code :: ${response.code()}")

            // Keep it separated so you can check if the request looks correct
            response
        }
        return this
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BasicAuthClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OauthClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultClient