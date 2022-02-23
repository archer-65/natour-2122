package com.unina.natourkt.presentation.profile.compilations

import androidx.paging.PagingData
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState

data class PersonalCompilationsUiState(
    val compilationItems: PagingData<CompilationItemUiState> = PagingData.empty()
)