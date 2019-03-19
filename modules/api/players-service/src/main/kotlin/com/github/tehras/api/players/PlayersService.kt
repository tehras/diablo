package com.github.tehras.api.players

import com.github.tehras.db.models.Player
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface PlayersService {
    @GET("/d3/profile/{battle_tag}/")
    fun getPlayer(
        @Path(value = "battle_tag") battleTag: String,
        @Query(value = "locale") locale: String = Locale.US.toString()
    ): Single<Player>
}
