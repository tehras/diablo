package com.github.tehras.api.icons

fun itemIconLg(itemName: String): String {
    return itemIcon(itemName, AssetSizes.LG)
}

fun itemIconSm(itemName: String): String {
    return itemIcon(itemName, AssetSizes.SM)
}

private fun itemIcon(itemName: String, size: AssetSizes): String {
    return ASSET_URL
        .replace(":type:", "items")
        .replace(":size:", if (size == AssetSizes.SM) "small" else "large")
        .replace(":icon:", itemName)
}