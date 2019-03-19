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
    val toughness: Long,
    val healing: Long,
    val attackSpeed: Double,
    val armor: Long,
    val strength: Double,
    val dexterity: Long,
    val vitality: Long,
    val intelligence: Long,
    val physicalResist: Long,
    val fireResist: Long,
    val coldResist: Long,
    val lightningResist: Long,
    val poisonResist: Long,
    val arcaneResist: Long,
    val blockChance: Double,
    val blockAmountMin: Long,
    val blockAmountMax: Long,
    val goldFind: Double,
    val critChance: Double,
    val thorns: Long,
    val lifeSteal: Long,
    val lifePerKill: Long,
    val lifeOnHit: Long,
    val primaryResource: Long,
    val secondaryResource: Long
) : Parcelable
