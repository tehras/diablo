package com.github.tehras.ui.leaderboards

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LeaderboardsListDataConverterTest {

    private lateinit var converter: LeaderboardsListDataConverter

    @Before
    fun setup() {
        converter = LeaderboardsListDataConverter()
    }

    @Test
    fun testTimeConverter() {
        assertThat("823733".toTime()).isEqualTo("13:43")
    }
}