package com.unina.natourkt.core.presentation.model

import com.unina.natourkt.core.util.DateTimeParser
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

fun List<MessageItemUi>.groupIntoMap(): List<ChatGenericUi> {
    val groupedHashMap: LinkedHashMap<String, MutableSet<MessageItemUi>> = LinkedHashMap()
    var list: MutableSet<MessageItemUi>

    for (message in this) {
        val hashMapKey: String = message.sentOn.toLocalDate().toString()
        if (groupedHashMap.containsKey(hashMapKey)) {
            groupedHashMap.get(hashMapKey)?.add(message)
        } else {
            list = LinkedHashSet()
            list.add(message)
            groupedHashMap.put(hashMapKey, list)
        }
    }

    return groupedHashMap.generateListFromMap()
}

fun LinkedHashMap<String, MutableSet<MessageItemUi>>.generateListFromMap(): List<ChatGenericUi> {
    val newList: MutableList<ChatGenericUi> = ArrayList()
    for (date in this.keys) {
        val dateItem =
            DateItemUi(date = DateTimeParser.parseDate(date))

        val set = this.get(date)
        if (set != null) {
            for (message in set) {
                newList.add(message)
            }
        }
        newList.add(dateItem)
    }

    return newList.toList()
}