/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.leaderboards

import com.github.tehras.api.leaderboards.models.LeaderboardsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author tkoshkin
 */
interface LeaderboardsService {
    @GET("/data/d3/season/{season_id}/leaderboard/{leaderboards_type}")
    fun getLeaderboard(
        @Path(value = "season_id") seasonId: String,
        @Path(value = "leaderboards_type") type: String
    ): Single<LeaderboardsResponse>
}
 