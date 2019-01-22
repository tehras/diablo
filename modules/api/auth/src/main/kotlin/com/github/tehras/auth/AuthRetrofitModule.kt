package com.github.tehras.auth

import com.github.tehras.common.UrlResolver
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object AuthRetrofitModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        rxJavaAdapter: RxJava2CallAdapterFactory,
        moshiAdapter: MoshiConverterFactory,
        urlResolver: UrlResolver
    ): Retrofit {
        val basicAuthOkHttp = okHttpClient
            .newBuilder()
            .addInterceptor(BasicAuthInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(urlResolver.baseUrl())
            .addConverterFactory(moshiAdapter)
            .client(basicAuthOkHttp)
            .addCallAdapterFactory(rxJavaAdapter)
            .build()
    }
}