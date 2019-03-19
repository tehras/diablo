package com.github.tehras.db.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "players")
@Parcelize
@JsonClass(generateAdapter = true)
data class Player(
    @PrimaryKey
    val battleTag: String,
    val paragonLevel: Int,
    val paragonLevelHardcore: Int,
    val paragonLevelSeasonHardcore: Int,
    val guildName: String,
    val heroes: List<Hero>,
    val lastHeroPlayed: Long,
    val lastUpdated: Long,
    @Embedded
    val kills: Kills,
    val highestHardcoreLevel: Int,
    @Embedded
    val timePlayed: TimePlayed,
    val seasonalProfiles: Map<String, SeasonalProfile>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Hero(
    val id: Long,
    val name: String,
    @Json(name = "class")
    val heroeClass: String,
    val gender: Int,
    val level: Int,
    val kills: Kills,
    @Json(name = "last-updated")
    val lastUpdated: Long,
    val seasonal: Boolean,
    val hardcore: Boolean,
    val paragonLevel: Int
) : Parcelable

fun Int.toGender() = if (this == 0) Gender.MALE else Gender.FEMALE

enum class Gender {
    MALE, FEMALE;

    companion object {
        fun random(): Gender {
            return if (Math.random() < 0.5) {
                return MALE
            } else {
                FEMALE
            }
        }
    }
}
