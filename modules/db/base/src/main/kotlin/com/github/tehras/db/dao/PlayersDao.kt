package com.github.tehras.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.tehras.db.models.Player
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: Player): Completable

    @Query("SELECT * FROM players")
    fun getAll(): Observable<List<Player>>
}