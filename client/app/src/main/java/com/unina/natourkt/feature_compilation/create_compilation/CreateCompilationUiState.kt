package com.unina.natourkt.feature_compilation.create_compilation

import android.net.Uri
import com.unina.natourkt.core.presentation.util.TextFieldState

data class CreateCompilationUiState(
    val isLoading: Boolean = false,
    val inInserted: Boolean = false,
    val compilationTitle: TextFieldState = TextFieldState(),
    val compilationDescription: TextFieldState = TextFieldState(),
    val compilationPhoto: Uri = Uri.EMPTY
) {

    val isButtonEnabled: Boolean
        get() = compilationTitle.text.isNotBlank() && compilationPhoto != Uri.EMPTY
}