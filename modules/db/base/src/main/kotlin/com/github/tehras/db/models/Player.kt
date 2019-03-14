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
    val heroes: List<Heroe>,
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
data class SeasonalProfile(
    val seasonId: Int,
    val paragonLevel: Int,
    val paragonLevelHardcore: Int,
    val kills: Kills,
    val timePlayed: TimePlayed,
    val highestHardcoreLevel: Int
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Heroe(
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

@Parcelize
@JsonClass(generateAdapter = true)
data class Kills(
    val monsters: Long?,
    val elites: Long?,
    val hardcoreMonsters: Long?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class TimePlayed(
    @Json(name = "demon-hunter")
    val demonHunter: Double,
    @Json(name = "witch-doctor")
    val witchDoctor: Double,
    val necromancer: Double,
    val barbarian: Double,
    val wizard: Double,
    val monk: Double,
    val crusader: Double
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
