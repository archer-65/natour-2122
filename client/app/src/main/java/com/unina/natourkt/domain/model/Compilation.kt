package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState

data class Compilation(
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val user: User
)

fun Compilation.toUi(): CompilationItemUiState{
    return CompilationItemUiState(
        id = id,
        title = title,
        description = description,
        photo = photo,
        authorId = user.id,
        authorPhoto = user.photo
    )
}
