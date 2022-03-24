package com.unina.natourkt.core.presentation.util

sealed class UiEffect {
    data class ShowSnackbar(val uiText: UiText) : UiEffect()
    data class ShowToast(val uiText: UiText) : UiEffect()
    object DismissDialog : UiEffect()
}