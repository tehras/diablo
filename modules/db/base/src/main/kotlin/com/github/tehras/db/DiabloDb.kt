package com.github.tehras.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.tehras.db.converters.HeroConverters
import com.github.tehras.db.converters.ItemDetailsConverters
import com.github.tehras.db.converters.PlayerConverters
import com.github.tehras.db.dao.HeroesDao
import com.github.tehras.db.dao.PlayersDao
import com.github.tehras.db.models.HeroDetails
import com.github.tehras.db.models.Player

@Database(entities = [Player::class, HeroDetails::class], version = 8)
@TypeConverters(value = [PlayerConverters::class, HeroConverters::class, ItemDetailsConverters::class])
abstract class DiabloDb : RoomDatabase() {

    abstract fun playersDao(): PlayersDao
    abstract fun heroesDao(): HeroesDao

    companion object {
        internal fun buildDatabase(context: Context): DiabloDb {
            return Room
                .databaseBuilder(
                    context.applicationContext,
                    DiabloDb::class.java,
                    "Diablo.db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}