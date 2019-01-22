/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.common

import com.github.tehras.dagger.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class UrlResolver @Inject constructor(private val regionPersistor: RegionPersistor) {
    fun baseUrl() = "https://${regionPersistor.currentRegion().name}.battle.net/"
}
