package com.github.tehras.base.arch.rx

sealed class NavEvent : GlobalEvent {
    data class StartPlayerDetailsScreen(val battleTag: String) : NavEvent()
}