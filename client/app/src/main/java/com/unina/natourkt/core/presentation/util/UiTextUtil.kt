package com.unina.natourkt.core.presentation.util

import android.content.Context


/**
 * Source:
 * https://github.com/philipplackner
 */
fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.DynamicString -> this.value
        is UiText.StringResource -> context.getString(this.id)
    }
}