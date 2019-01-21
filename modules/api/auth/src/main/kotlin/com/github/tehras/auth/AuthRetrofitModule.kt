package com.github.tehras.auth

import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object AuthRetrofitModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
}