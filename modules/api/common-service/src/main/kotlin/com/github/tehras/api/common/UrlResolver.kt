/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.api.common

import com.github.tehras.dagger.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class UrlResolver @Inject constructor(private val regionPersistor: RegionPersistor) {
    fun authUrl() = "https://${regionPersistor.currentRegion().name}.battle.net/"
    fun apiUrl() = "https://${regionPersistor.currentRegion().name}.api.blizzard.com/"
}
