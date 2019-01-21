package com.github.tehras.dagger.components

/**
 * @author tkoshkin created on 8/24/18
 */
interface ComponentProvider<Component> {
    fun getComponent(): Component
}