package com.github.tehras.api.icons

internal const val ASSET_URL = "http://media.blizzard.com/d3/icons/:type:/:size:/:icon:.png"

internal enum class AssetSizes(val size: Int) {
    SM(21), MD(42), LG(64)
}