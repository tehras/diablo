package com.github.tehras.ui.leaderboards

import com.google.common.truth.Truth
import org.junit.Test

class LeaderboardsListDataConverterTest {

    @Test
    fun testTimeConverter() {
        Truth.assertThat("823733".toTime()).isEqualTo("13:43")
    }
}