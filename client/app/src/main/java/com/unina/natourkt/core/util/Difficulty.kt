package com.unina.natourkt.core.util

enum class Difficulty(val value: Int) {
    EASY(1),
    MEDIUM(2),
    HARD(3),
    NONE(0);

    companion object {
        fun fromInt(value: Int) = Difficulty.values().first { it.value == value }
    }
}