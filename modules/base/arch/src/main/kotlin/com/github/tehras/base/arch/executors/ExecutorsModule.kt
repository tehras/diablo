package com.github.tehras.base.arch.executors

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
class ExecutorsModule {
    @DbExectutor
    @Provides
    fun provideDbExecutor(): Scheduler = Schedulers.io()

    @NetworkExecutor
    @Provides
    fun provideNetworkExecutor(): Scheduler = Schedulers.io()
}

