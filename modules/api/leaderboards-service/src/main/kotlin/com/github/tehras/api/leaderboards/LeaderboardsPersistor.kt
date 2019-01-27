package com.github.tehras.api.leaderboards

import android.content.SharedPreferences
import javax.inject.Inject

class LeaderboardsPersistor @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun updateLeaderboards(leaderboardsType: LeaderboardsType) {
        sharedPreferences
            .edit()
            .putInt(LEADERBOARDS_ARG, leaderboardsType.ordinal)
            .apply()
    }

    /**
     * This API will return the currently selected Leadoardserb
     * If there is no leaderboards selected, [LeaderboardsType.SOLO] will be returned
     */
    fun currentLeaderboards(): LeaderboardsType {
        val leaderboards =
            sharedPreferences.getInt(LEADERBOARDS_ARG, LeaderboardsType.QUADS.ordinal) // US is the default
        return LeaderboardsType.values()[leaderboards]
    }

    companion object {
        private const val LEADERBOARDS_ARG = "selected_leaderboards_type"
    }
}

enum class LeaderboardsType(val type: String) {
    SOLO_BARB("rift-barbarian"),
    SOLO_CRUSADER("rift-crusader"),
    SOLO_NECRO("rift-necromancer"),
    SOLO_WIZARD("rift-wizard"),
    SOLO_WITCH_DCTOR("rift-wd"),
    SOLO_MONK("rift-monk"),
    DUOS("rift-team-2"),
    THREES("rift-team-3"),
    QUADS("rift-team-4")
}