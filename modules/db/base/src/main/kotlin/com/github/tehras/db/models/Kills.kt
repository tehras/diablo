package com.github.tehras.db.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Kills(
    val monsters: Long?,
    val elites: Long?,
    val hardcoreMonsters: Long?
) : Parcelable