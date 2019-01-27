/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.common

import android.content.SharedPreferences
import javax.inject.Inject

class RegionPersistor @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun updateRegion(region: Region) {
        sharedPreferences
            .edit()
            .putString(REGION_ARG, region.name)
            .apply()
    }

    /**
     * This API will return the currently selected Region
     * If there is no regions selected, [Region.US] will be returned
     */
    fun currentRegion(): Region {
        val region = sharedPreferences.getString(REGION_ARG, Region.US.name) ?: Region.US.name // US is the default
        return Region.values().first { region == it.name }
    }

    companion object {
        private const val REGION_ARG = "selected_region"
    }
}

enum class Region(name: String) {
    US("us")
}

