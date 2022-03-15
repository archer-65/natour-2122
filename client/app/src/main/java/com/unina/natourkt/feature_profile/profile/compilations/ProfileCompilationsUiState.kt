package com.unina.natourkt.feature_profile.profile.compilations

import androidx.paging.PagingData
import com.unina.natourkt.core.presentation.model.CompilationItemUi

data class ProfileCompilationsUiState(
    val compilationItems: PagingData<CompilationItemUi> = PagingData.empty()
)