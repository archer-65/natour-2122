package com.unina.natourkt.feature_compilation.compilation_details

data class CompilationDetailsUiState(
    val isLoading: Boolean = false,
    val isRemoved: Boolean = false,
    val removedPosition: Int? = null
)