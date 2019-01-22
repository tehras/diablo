/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.auth

import com.github.tehras.common.CLIENT_ID
import com.github.tehras.common.CLIENT_SECRET
import okhttp3.Credentials
import java.io.IOException
import okhttp3.Interceptor.Chain
import okhttp3.Interceptor
import okhttp3.Response


class BasicAuthInterceptor : Interceptor {
    private val credentials: String = Credentials.basic(CLIENT_ID, CLIENT_SECRET)

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request
            .newBuilder()
            .header("Authorization", credentials)
            .build()

        return chain.proceed(authenticatedRequest)
    }

}