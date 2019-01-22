/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.auth

import android.content.SharedPreferences
import com.github.tehras.auth.models.OauthResponse
import com.github.tehras.dagger.scopes.ApplicationScope
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ApplicationScope
class OauthTokenProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authService: AuthService,
    private val moshi: Moshi
) {
    fun oauthToken(): Single<String> {
        return Single
            .fromCallable {
                persistedToken()
            }
            .flatMap { token ->
                if (token.isEmpty()) {
                    authService
                        .oathToken()
                        .doOnSuccess { saveToken(it) }
                        .map { it.accessToken }
                } else {
                    Single.just(token)
                }
            }
    }

    private fun persistedToken(): String {
        val savedOauthResponse = sharedPreferences
            .getString(TOKEN_ARG, null) ?: return ""

        // Check if expired
        val restoredOauth = moshi.adapter(PersistedOauth::class.java).fromJson(savedOauthResponse)
        if (restoredOauth == null || isExpired(restoredOauth)) {
            clearTokenFromPrefs()
            return ""
        }

        return restoredOauth.token
    }

    private fun clearTokenFromPrefs() {
        sharedPreferences
            .edit()
            .remove(TOKEN_ARG)
            .apply()
    }

    private fun isExpired(persistedOauth: PersistedOauth) =
        persistedOauth.expiration > (System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1L))

    private fun saveToken(oauthResponse: OauthResponse) {
        // convert response to a meaningful object to persist
        val expiresIn = oauthResponse.expiresIn // this is in seconds

        val expiration = System.currentTimeMillis().plus(TimeUnit.SECONDS.toMillis(expiresIn.toLong()))
        val persistedOauth = PersistedOauth(oauthResponse.accessToken, expiration)
        val jsonOauth = moshi.adapter(PersistedOauth::class.java).toJson(persistedOauth)

        sharedPreferences
            .edit()
            .putString(TOKEN_ARG, jsonOauth)
            .apply()
    }

    companion object {
        private const val TOKEN_ARG = "saved_token"
    }

    @JsonClass(generateAdapter = true)
    data class PersistedOauth(val token: String, val expiration: Long)
}