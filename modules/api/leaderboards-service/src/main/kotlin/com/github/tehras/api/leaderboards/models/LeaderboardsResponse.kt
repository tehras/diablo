/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.leaderboards.models

import com.squareup.moshi.JsonClass

/**
 * @author tkoshkin
 */
@JsonClass(generateAdapter = true)
data class LeaderboardsResponse(
    val row: List<Row>
)

@JsonClass(generateAdapter = true)
data class Row(
    val player: List<Player>
)

@JsonClass(generateAdapter = true)
data class Player(
    val key: String,
    val accountId: String,
    val data: List<Data>
)

@JsonClass(generateAdapter = true)
data class Data(
    val id: String,
    val string: String?,
    val number: Int?
)