package com.github.tehras.api.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BasicAuthClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OauthClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultClient