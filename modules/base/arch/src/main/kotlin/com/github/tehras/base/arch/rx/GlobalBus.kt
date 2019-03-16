package com.github.tehras.base.arch.rx

import com.github.tehras.dagger.scopes.ApplicationScope
import com.jakewharton.rxrelay2.PublishRelay
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.functions.Consumer

@Module
object GlobalBusModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesGlobalBus() = GlobalBus()
}

interface GlobalBusInjector {
    fun globalBus(): GlobalBus
}

class GlobalBus : Consumer<GlobalEvent> {
    private val relay = PublishRelay.create<GlobalEvent>()

    override fun accept(event: GlobalEvent) {
        relay.accept(event)
    }

    fun observe(): Observable<GlobalEvent> = relay.shareBehavior()
}

interface GlobalEvent