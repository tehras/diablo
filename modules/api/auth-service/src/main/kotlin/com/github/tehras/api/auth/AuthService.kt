package com.github.tehras.api.auth

import com.github.tehras.api.auth.models.OauthResponse
import io.reactivex.Single
import retrofit2.http.*

interface AuthService {
    @POST("/oauth/token")
    @FormUrlEncoded
    fun oathToken(@Field("grant_type") grantType: String = "client_credentials"): Single<OauthResponse>
}