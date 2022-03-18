package com.unina.natourkt.feature_compilation.save_into_compilation

import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi

data class SaveIntoCompilationUiState(
    val compilations: List<CompilationDialogItemUi> = emptyList(),
)