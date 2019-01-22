/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.auth.interceptors

import com.github.tehras.api.auth.oauth.OauthTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class OauthInterceptor(private val oauthTokenProvider: OauthTokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request
            .newBuilder()
            .header("Authorization", "Bearer ${oauthTokenProvider.oauthTokenImmediately()}")
            .build()

        return chain.proceed(authenticatedRequest)
    }

}