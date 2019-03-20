package com.github.tehras.db.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "heroes")
@JsonClass(generateAdapter = true)
data class HeroDetails(
    @PrimaryKey
    val id: Long,
    val name: String,
    @Json(name = "class")
    val heroeClass: String,
    val gender: Int,
    val level: Int,
    @Embedded
    val kills: Kills,
    val lastUpdated: Long?,
    val seasonal: Boolean,
    val hardcore: Boolean,
    val paragonLevel: Int,
    val seasonCreated: Int,
    @Embedded
    val skills: Skills,
    val items: Map<String, Item>,
    val legendaryPowers: List<Item>,
    val highestSoloRiftCompleted: Int,
    @Embedded
    val stats: Stats
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Skills(
    val active: List<Active>,
    val passive: List<Passive>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Active(
    @Embedded
    val skill: Skill,
    @Embedded
    val rune: Rune
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Passive(
    @Embedded
    val skill: Skill
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Skill(
    val slug: String,
    val name: String,
    val icon: String,
    val level: Int,
    val tooltipUrl: String,
    val description: String,
    val descriptionHtml: String,
    val flavorText: String?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Rune(
    val slug: String,
    val type: String,
    val name: String,
    val level: Int,
    val description: String,
    val descriptionHtml: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Item(
    val id: String,
    val name: String,
    val icon: String,
    val displayColor: String,
    val tooltipParams: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Stats(
    val life: Double,
    val damage: Double,
    val toughness: Double,
    val healing: Double,
    val attackSpeed: Double,
    val armor: Double,
    val strength: Double,
    val dexterity: Double,
    val vitality: Double,
    val intelligence: Double,
    val physicalResist: Double,
    val fireResist: Double,
    val coldResist: Double,
    val lightningResist: Double,
    val poisonResist: Double,
    val arcaneResist: Double,
    val blockChance: Double,
    val blockAmountMin: Double,
    val blockAmountMax: Double,
    val goldFind: Double,
    val critChance: Double,
    val thorns: Double,
    val lifeSteal: Double,
    val lifePerKill: Double,
    val lifeOnHit: Double,
    val primaryResource: Double,
    val secondaryResource: Double
) : Parcelable
