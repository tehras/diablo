package com.github.tehras.db.converters

import androidx.room.TypeConverter
import com.github.tehras.db.models.Heroe
import com.github.tehras.db.models.SeasonalProfile
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class PlayerConverters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toListOfHeroes(json: String?): List<Heroe> {
        return if (json.isNullOrEmpty()) {
            listOf()
        } else {
            val listMyData = Types.newParameterizedType(List::class.java, Heroe::class.java)
            moshi.adapter<List<Heroe>>(listMyData).fromJson(json) ?: listOf()
        }
    }

    @TypeConverter
    fun fromListOfHeroes(heroes: List<Heroe>?): String {
        if (heroes.isNullOrEmpty()) return ""

        val listMyData = Types.newParameterizedType(List::class.java, Heroe::class.java)
        return moshi.adapter<List<Heroe>>(listMyData).toJson(heroes)
    }

    @TypeConverter
    fun toMapOfProfiles(json: String?): Map<String, SeasonalProfile> {
        if (json.isNullOrEmpty()) return mapOf()

        val mapMyData = Types.newParameterizedType(Map::class.java, String::class.java, SeasonalProfile::class.java)
        return moshi.adapter<Map<String, SeasonalProfile>>(mapMyData).fromJson(json) ?: mapOf()
    }

    @TypeConverter
    fun fromMapOfProfiles(profiles: Map<String, SeasonalProfile>?): String {
        if (profiles.isNullOrEmpty()) return ""

        val mapMyData = Types.newParameterizedType(Map::class.java, String::class.java, SeasonalProfile::class.java)
        return moshi.adapter<Map<String, SeasonalProfile>>(mapMyData).toJson(profiles)
    }
}