package com.github.tehras.api.heroes

import com.github.tehras.db.models.HeroDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface HeroDetailsService {
    @GET("/d3/profile/{battle_tag}/hero/{hero_id}")
    fun getHeroDetails(
        @Path(value = "battle_tag") battleTag: String,
        @Path(value = "hero_id") heroId: String,
        @Query(value = "locale") locale: String = Locale.US.toString()
    ): Single<HeroDetails>
}