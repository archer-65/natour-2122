package com.unina.natourkt.core.presentation.util

import com.unina.natourkt.core.util.Event

sealed class UiEvent : Event() {
    data class ShowSnackbar(val uiText: UiText) : UiEvent()
    object NavigateUp : UiEvent()
}