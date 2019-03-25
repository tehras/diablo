package com.github.tehras.api.items

import com.github.tehras.api.common.PrivateRetrofit
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object ItemsServiceModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesItemsService(@PrivateRetrofit retrofit: Retrofit): ItemsService =
        retrofit.create(ItemsService::class.java)
}