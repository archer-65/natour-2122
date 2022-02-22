package com.unina.natourkt.presentation.base.ui_state

import com.unina.natourkt.domain.model.User

data class CompilationItemUiState (
    val id: Long,
    val title: String,
    val description: String,
    val photoUrl: String,
    val userId: Long,
    val userPhotoUrl: String?
)