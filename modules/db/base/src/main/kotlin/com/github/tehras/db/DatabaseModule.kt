package com.github.tehras.db

import android.content.Context
import com.github.tehras.dagger.scopes.ApplicationScope
import com.github.tehras.db.dao.HeroesDao
import com.github.tehras.db.dao.PlayersDao
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {
    @Provides
    @ApplicationScope
    @JvmStatic
    fun provideDatabase(context: Context): DiabloDb = DiabloDb.buildDatabase(context)

    @Provides
    @JvmStatic
    fun providePlayersDao(diabloDb: DiabloDb): PlayersDao = diabloDb.playersDao()

    @Provides
    @JvmStatic
    fun provideHeroesDao(diabloDb: DiabloDb): HeroesDao = diabloDb.heroesDao()
}