package com.github.tehras.db.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

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