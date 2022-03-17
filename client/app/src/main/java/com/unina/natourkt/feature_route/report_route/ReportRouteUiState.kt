package com.unina.natourkt.feature_route.report_route

import com.unina.natourkt.core.presentation.util.TextFieldState

data class ReportRouteUiState(
    val isLoading: Boolean = false,
    val isInserted: Boolean = false,
    val reportTitle: TextFieldState = TextFieldState(),
    val reportDescription: TextFieldState = TextFieldState()
) {

    val isButtonEnabled: Boolean
        get() = reportTitle.text.isNotBlank() && reportDescription.text.isNotBlank()
}