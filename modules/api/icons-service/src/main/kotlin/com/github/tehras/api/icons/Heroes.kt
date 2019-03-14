package com.github.tehras.api.icons

fun heroIconMd(hero: String, gender: String): String {
    return heroIcon(hero, gender, AssetSizes.MD)
}

fun heroIconLg(hero: String, gender: String): String {
    return heroIcon(hero, gender, AssetSizes.LG)
}

private fun heroIcon(hero: String, gender: String, size: AssetSizes): String {
    return ASSET_URL
        .replace(":type:", "portraits")
        .replace(":size:", "${size.size}")
        .replace(
            ":icon:", "${hero
                .replace(" ", "")
                .replace("necromancer", "p6_necro")
                .replace("crusader", "x1_crusader")}_$gender"
        )
}