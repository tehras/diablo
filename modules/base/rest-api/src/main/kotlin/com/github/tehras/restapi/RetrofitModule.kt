package com.github.tehras.restapi

import com.github.tehras.dagger.scopes.ApplicationScope
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

/**
 * @author tkoshkin created on 8/26/18
 */
@Module
abstract class RetrofitModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        @ApplicationScope
        fun providesOkHttp(): OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor {
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
                    return@addNetworkInterceptor response
                }
                .build()
        }

        @Provides
        @JvmStatic
        @ApplicationScope
        fun providesRxConverter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        @Provides
        @JvmStatic
        @ApplicationScope
        fun providesMoshiConverter(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)
    }
}