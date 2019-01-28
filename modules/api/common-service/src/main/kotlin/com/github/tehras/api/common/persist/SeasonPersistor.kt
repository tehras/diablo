package com.github.tehras.api.common.persist

import android.content.SharedPreferences
import javax.inject.Inject


class SeasonPersistor @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun updateSeason(season: String) {
        sharedPreferences
            .edit()
            .putString(SEASON_ARG, season)
            .apply()
    }

    fun currentSeason(): String = sharedPreferences.getString(SEASON_ARG, "16") ?: "16"

    companion object {
        private const val SEASON_ARG = "selected_season"
    }
}
