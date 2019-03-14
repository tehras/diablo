package com.github.tehras.ui.leaderboards

import androidx.annotation.VisibleForTesting
import com.github.tehras.api.icons.heroIconMd
import com.github.tehras.api.leaderboards.models.Data
import com.github.tehras.api.leaderboards.models.LeaderboardsResponse
import com.github.tehras.api.leaderboards.models.Player
import com.github.tehras.api.leaderboards.models.Row
import com.github.tehras.dagger.scopes.FragmentScope
import com.github.tehras.ui.leaderboards.adapter.LeaderboardsBody
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class LeaderboardsListDataConverter @Inject constructor() {

    private var lastRank = 0

    @Synchronized
    fun convertToUiData(remoteData: LeaderboardsResponse): List<LeaderboardsBody> {
        lastRank = 0
        return remoteData
            .row
            .filter { it.data[0].id == "Rank" }
            .map {
                lastRank++
                convertToBody(it)
            }
    }

    private fun convertToBody(
        row: Row
    ): LeaderboardsBody {
        val mappedData = row.data.convertToMap()

        return LeaderboardsBody(
            rank = lastRank.toString(),
            riftLevel = mappedData["RiftLevel"] ?: "N/A",
            riftTime = mappedData["RiftTime"].toTime(),
            completedTime = mappedData["CompletedTime"].toDate(),
            players = convertToPlayers(row.player)
        )
    }

    private fun convertToPlayers(players: List<Player>): List<LeaderboardsBody.LeaderboardsPlayer> {
        return players
            .map {
                val mappedData = it.data.convertToMap()
                LeaderboardsBody.LeaderboardsPlayer(
                    name = simplifyName(mappedData["HeroBattleTag"], mappedData["HeroClanTag"]),
                    heroImageUrl = generateHeroUrl(mappedData["HeroClass"], mappedData["HeroGender"]),
                    heroClass = mappedData["HeroClass"]?.capitalize() ?: "",
                    paragonLevel = mappedData["ParagonLevel"] ?: ""
                )
            }
    }

    private fun generateHeroUrl(heroClass: String?, gender: String?): String {
        if (heroClass == null || gender == null) return ""

        return heroIconMd(heroClass, gender.let { if (it == "f") "female" else "male" })
    }

    private fun simplifyName(battleTag: String?, clanTag: String?): String {
        val name = battleTag?.substringBefore("#") ?: "Unknown"
        val clan = clanTag?.let {
            "$it."
        } ?: ""

        return "$clan$name"
    }
}

@VisibleForTesting
fun String?.toTime(): String {
    if (this == null) return "N/A"

    toIntOrNull()?.run {
        val seconds = div(1000)
        val mins = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val remainingSeconds = seconds - (mins * 60)

        return "$mins:$remainingSeconds"
    } ?: return "N/A"
}

@VisibleForTesting
fun String?.toDate(): String {
    if (this == null) return ""

    toLongOrNull()?.run {
        return SimpleDateFormat("MMM dd 'at' hh:mma", Locale.US).format(Date(this))
    } ?: return ""
}

private fun List<Data>.convertToMap(): HashMap<String, String?> {
    val map = hashMapOf<String, String?>()
    this
        .forEach {
            map[it.id] = it.string ?: (it.number?.toString() ?: it.timestamp?.toString())
        }

    return map
}
