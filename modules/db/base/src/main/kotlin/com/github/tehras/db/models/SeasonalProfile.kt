package com.github.tehras.db.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

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