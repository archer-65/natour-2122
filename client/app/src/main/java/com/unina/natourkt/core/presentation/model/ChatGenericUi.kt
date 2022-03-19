package com.unina.natourkt.core.presentation.model

import java.lang.IllegalStateException

interface ChatGenericUi {
    val type: MessageType

    override fun equals(other: Any?): Boolean
}

enum class MessageType(val id: Int) {
    TYPE_DATE(0),
    TYPE_ME(1),
    TYPE_OTHER(2);

    companion object {
        fun create(id: Int): MessageType {
            return when (id) {
                0 -> TYPE_DATE
                1 -> TYPE_ME
                2 -> TYPE_OTHER
                else -> throw IllegalStateException()
            }
        }
    }
}