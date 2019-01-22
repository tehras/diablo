package com.github.tehras.auth

import com.github.tehras.auth.models.OauthResponse
import com.github.tehras.dagger.scopes.ApplicationScope
import io.reactivex.Single
import retrofit2.http.*

interface AuthService {
    @POST("/oauth/token")
    @FormUrlEncoded
    fun oathToken(@Field("grant_type") grantType: String = "client_credentials"): Single<OauthResponse>
}