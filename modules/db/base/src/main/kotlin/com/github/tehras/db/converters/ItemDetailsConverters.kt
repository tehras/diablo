package com.github.tehras.db.converters

import androidx.room.TypeConverter
import com.github.tehras.db.models.ItemAttribute
import com.github.tehras.db.models.RandomAffix
import com.github.tehras.db.models.SetItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ItemDetailsConverters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toListOfAttributes(json: String?): List<ItemAttribute> {
        if (json.isNullOrEmpty()) return listOf()

        val listMyData = Types.newParameterizedType(List::class.java, ItemAttribute::class.java)
        return moshi.adapter<List<ItemAttribute>>(listMyData).fromJson(json) ?: listOf()
    }

    @TypeConverter
    fun fromListOfItems(items: List<ItemAttribute>?): String {
        if (items.isNullOrEmpty()) return ""

        val listMyData = Types.newParameterizedType(List::class.java, ItemAttribute::class.java)
        return moshi.adapter<List<ItemAttribute>>(listMyData).toJson(items)
    }

    @TypeConverter
    fun toListOfRandomAffix(json: String?): List<RandomAffix> {
        if (json.isNullOrEmpty()) return listOf()

        val listMyData = Types.newParameterizedType(List::class.java, RandomAffix::class.java)
        return moshi.adapter<List<RandomAffix>>(listMyData).fromJson(json) ?: listOf()
    }

    @TypeConverter
    fun fromListOfRandomAffix(items: List<RandomAffix>?): String {
        if (items.isNullOrEmpty()) return ""

        val listMyData = Types.newParameterizedType(List::class.java, RandomAffix::class.java)
        return moshi.adapter<List<RandomAffix>>(listMyData).toJson(items)
    }

    @TypeConverter
    fun toListOSetItem(json: String?): List<SetItem> {
        if (json.isNullOrEmpty()) return listOf()

        val listMyData = Types.newParameterizedType(List::class.java, SetItem::class.java)
        return moshi.adapter<List<SetItem>>(listMyData).fromJson(json) ?: listOf()
    }

    @TypeConverter
    fun fromListOfSetItem(items: List<SetItem>?): String {
        if (items.isNullOrEmpty()) return ""

        val listMyData = Types.newParameterizedType(List::class.java, SetItem::class.java)
        return moshi.adapter<List<SetItem>>(listMyData).toJson(items)
    }
}