package com.github.tehras.base.arch.executors

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DbExectutor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkExecutor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainThreadExecutor