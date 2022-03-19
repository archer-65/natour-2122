package com.unina.natourkt.core.presentation.model

import java.time.LocalDateTime

data class MessageItemUi(
    val messageId: Long,
    val sentOn: LocalDateTime,
    val content: String,
    override val type: MessageType,
) : ChatGenericUi

//enum class MessageType(val id: Int) {
//    TYPE_ME(1),
//    TYPE_OTHER(2);
//
//    companion object {
//        fun create(id: Int): MessageType {
//            return when (id) {
//                1 -> TYPE_ME
//                2 -> TYPE_OTHER
//                else -> throw IllegalStateException()
//            }
//        }
//    }
//}