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

enum class LeaderboardsType(val serverType: String, val description: String) {
    SOLO_BARB("rift-barbarian", "Barbarian"),
    SOLO_CRUSADER("rift-crusader", "Crusader"),
    SOLO_MONK("rift-monk", "Monk"),
    SOLO_NECRO("rift-necromancer", "Necromancer"),
    SOLO_WIZARD("rift-wizard", "Wizard"),
    SOLO_WITCH_DOCTOR("rift-wd", "Witch Doctor"),
    DUOS("rift-team-2", "Duos"),
    THREES("rift-team-3", "Threes"),
    QUADS("rift-team-4", "Squads")
}