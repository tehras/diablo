package com.github.tehras.api.icons

fun skillIconLg(skillName: String): String {
    return skillIcon(skillName, AssetSizes.LG)
}

fun skillIconSm(skillName: String): String {
    return skillIcon(skillName, AssetSizes.SM)
}

fun skillIconMd(skillName: String): String {
    return skillIcon(skillName, AssetSizes.MD)
}

private fun skillIcon(skillName: String, size: AssetSizes): String {
    return ASSET_URL
        .replace(":type:", "skills")
        .replace(":size:", size.size.toString())
        .replace(":icon:", skillName)
}