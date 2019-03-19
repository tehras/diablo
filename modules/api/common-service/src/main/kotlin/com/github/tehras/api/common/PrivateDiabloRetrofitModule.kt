package com.github.tehras.api.common

import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@Module
object PrivateDiabloRetrofitModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    @PrivateRetrofit
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
annotation class PrivateRetrofit