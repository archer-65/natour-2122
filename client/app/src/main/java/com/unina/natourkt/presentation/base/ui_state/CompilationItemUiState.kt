package com.unina.natourkt.presentation.base.ui_state

import com.unina.natourkt.domain.model.User

data class CompilationItemUiState (
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val authorId: Long,
    val authorPhoto: String?
)