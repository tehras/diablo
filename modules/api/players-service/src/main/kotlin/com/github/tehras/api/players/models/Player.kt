package com.github.tehras.api.players.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Player(
    val battleTag: String,
    val paragonLevel: Int,
    val paragonLevelHardcore: Int,
    val paragonLevelSeasonHardcore: Int,
    val guildName: String,
    val heroes: List<Heroe>,
    val lastHeroPlayed: Long,
    val lastUpdated: Long,
    val kills: Kills,
    val highestHardcoreLevel: Int,
    val timePlayed: TimePlayed,
    val seasonalProfiles: Map<String, SeasonalProfile>
)

@JsonClass(generateAdapter = true)
data class SeasonalProfile(
    val seasonId: Int,
    val paragonLevel: Int,
    val paragonLevelHardcore: Int,
    val kills: Kills,
    val timePlayed: TimePlayed,
    val highestHardcoreLevel: Int
)

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
)

@JsonClass(generateAdapter = true)
data class Kills(
    val monsters: Long?,
    val elites: Long?,
    val hardcoreMonsters: Long?
)

@JsonClass(generateAdapter = true)
data class TimePlayed(
    @Json(name = "demon-hunter")
    val demonHunter: Double,
    @Json(name = "witch-doctor")
    val witchDoctor: Double,
    val necromancer: Double,
    val wizard: Double,
    val monk: Double,
    val crusader: Double
)

fun Int.toGender() = if (this == 0) Gender.MALE else Gender.FEMALE

enum class Gender { MALE, FEMALE }

