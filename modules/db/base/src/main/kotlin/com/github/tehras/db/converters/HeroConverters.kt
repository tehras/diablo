package com.github.tehras.db.converters

import androidx.room.TypeConverter
import com.github.tehras.db.models.Active
import com.github.tehras.db.models.Item
import com.github.tehras.db.models.Passive
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class HeroConverters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toMapOfItems(json: String?): Map<String, Item> {
        if (json.isNullOrEmpty()) return mapOf()

        val mapMyData = Types.newParameterizedType(Map::class.java, String::class.java, Item::class.java)
        return moshi.adapter<Map<String, Item>>(mapMyData).fromJson(json) ?: mapOf()
    }

    @TypeConverter
    fun fromMapOfItems(items: Map<String, Item>?): String {
        if (items.isNullOrEmpty()) return ""

        val mapMyData = Types.newParameterizedType(Map::class.java, String::class.java, Item::class.java)
        return moshi.adapter<Map<String, Item>>(mapMyData).toJson(items)
    }

    @TypeConverter
    fun toListOfActives(json: String?): List<Active> {
        return if (json.isNullOrEmpty()) {
            listOf()
        } else {
            val listMyData = Types.newParameterizedType(List::class.java, Active::class.java)
            moshi.adapter<List<Active>>(listMyData).fromJson(json) ?: listOf()
        }
    }

    @TypeConverter
    fun fromListOfActives(actives: List<Active>?): String {
        if (actives.isNullOrEmpty()) return ""

        val listMyData = Types.newParameterizedType(List::class.java, Active::class.java)
        return moshi.adapter<List<Active>>(listMyData).toJson(actives)
    }

    @TypeConverter
    fun toListOfPassives(json: String?): List<Passive> {
        return if (json.isNullOrEmpty()) {
            listOf()
        } else {
            val listMyData = Types.newParameterizedType(List::class.java, Passive::class.java)
            moshi.adapter<List<Passive>>(listMyData).fromJson(json) ?: listOf()
        }

    }

    @TypeConverter
    fun fromListOfPassives(passives: List<Passive>?): String {
        if (passives.isNullOrEmpty()) return ""

        val listMyData = Types.newParameterizedType(List::class.java, Passive::class.java)
        return moshi.adapter<List<Passive>>(listMyData).toJson(passives)
    }
}