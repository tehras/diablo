package com.github.tehras.api.items

import com.github.tehras.db.models.ItemDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface ItemsService {
    @GET("/d3/data/item/{items_slug_and_id}")
    fun getItem(
        @Path(value = "items_slug_and_id") itemsSlugAndId: String,
        @Query(value = "locale") locale: String = Locale.US.toString()
    ): Single<ItemDetails>
}