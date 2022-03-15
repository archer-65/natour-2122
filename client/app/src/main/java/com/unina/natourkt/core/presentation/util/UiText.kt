package com.unina.natourkt.core.presentation.util

import androidx.annotation.StringRes
import com.unina.natourkt.R

/**
 * Source:
 * https://github.com/philipplackner
 */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(@StringRes val id: Int) : UiText()

    companion object {
        fun unknownError(): UiText {
            return UiText.StringResource(R.string.unknown_error)
        }
    }
}
