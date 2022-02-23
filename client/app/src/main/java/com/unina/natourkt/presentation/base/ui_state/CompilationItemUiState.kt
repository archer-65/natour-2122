package com.unina.natourkt.presentation.base.ui_state

data class CompilationItemUiState (
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val authorId: Long,
    val authorPhoto: String?
)