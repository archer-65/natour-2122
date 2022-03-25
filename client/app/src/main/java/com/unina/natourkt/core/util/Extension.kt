package com.unina.natourkt.core.util

import android.net.Uri
import android.webkit.URLUtil
import com.unina.natourkt.core.presentation.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun <T> List<T>.safeRemove(position: Int): List<T> {
    val newList = this.toMutableList().apply {
        removeAt(position)
    }.toList()

    return newList
}

suspend fun String.convertKeyToUrl(execute: suspend (text: String) -> String): String {
    return if (!URLUtil.isValidUrl(this)) {
        execute(this)
    } else {
        this
    }
}

suspend fun Uri.toInputStream() = withContext(Dispatchers.Default) {
    runCatching {
        MainActivity.instance.contentResolver.openInputStream(this@toInputStream)
    }
}