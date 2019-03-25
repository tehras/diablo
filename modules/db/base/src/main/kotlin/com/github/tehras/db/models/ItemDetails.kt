package com.github.tehras.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "itemDetails")
data class ItemDetails(
    @PrimaryKey
    val id: String,
    val name: String,
    val slug: String,
    val icon: String,
    val tooltipParams: String,
    val requiredLevel: Int,
    val stackSizeMax: Int,
    val accountBound: Boolean,
    val flavorTextHtml: String,
    val typeName: String,
    @Embedded
    val type: ItemType,
    val damage: String?,
    val armor: String?,
    val dps: String?,
    val damageHtml: String?,
    val armorHtml: String?,
    val color: String,
    val isSeasonRequiredToDrop: Boolean,
    val seasonRequiredToDrop: Int,
    @Embedded
    val slots: List<String>,
    @Embedded
    val attributes: ItemAttributes,
    @Embedded
    val randomAffixes: List<RandomAffix>,
    val setNameHtml: String?,
    val setDescriptionHtml: String?
//    @Embedded
//    val setItems: List<SetItem>
)

@JsonClass(generateAdapter = true)
class SetItem

@JsonClass(generateAdapter = true)
data class RandomAffix(
    val oneOf: List<ItemAttribute>
)

@JsonClass(generateAdapter = true)
data class ItemType(
    val twoHanded: Boolean,
    val id: String
)

@JsonClass(generateAdapter = true)
data class ItemAttributes(
    val primary: List<ItemAttribute>,
    val secondary: List<ItemAttribute>,
    val other: List<ItemAttribute>
)

@JsonClass(generateAdapter = true)
data class ItemAttribute(
    val textHtml: String,
    val text: String
)