package com.unina.natourkt.feature_route.routes

import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi

data class RoutesUiState(
    val isLoading: Boolean = false,
    val compilations: List<CompilationDialogItemUi> = emptyList(),
    val selectedRoute: Long? = null
)