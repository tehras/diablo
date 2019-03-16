package com.github.tehras.db.dao

import androidx.room.*
import com.github.tehras.db.models.Player
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: Player): Completable

    @Delete
    fun delete(player: Player): Completable

    @Query("SELECT * FROM players")
    fun getAll(): Observable<List<Player>>

    @Query("SELECT * FROM players WHERE battleTag IS :battleTag")
    fun getBy(battleTag: String): Observable<Player>
}