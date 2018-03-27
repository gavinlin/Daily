package com.gavincode.bujo.presentation.util

data class Message(val level: Level, val title: String, val content: String) {
    enum class Level {
        LOG, WARNING, ERROR
    }
}