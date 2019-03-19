package com.github.tehras.db.dao

import androidx.room.*
import com.github.tehras.db.models.HeroDetails
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface HeroesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hero: HeroDetails): Completable

    @Delete
    fun delete(hero: HeroDetails): Completable

    @Query("SELECT * FROM heroes WHERE id IS :id")
    fun getBy(id: Long): Observable<HeroDetails>
}