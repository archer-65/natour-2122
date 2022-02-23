package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState

data class Compilation(
    val id: Long,
    val title: String,
    val description: String,
    val photoUrl: String,
    val user: User
)

fun Compilation.toUi(): CompilationItemUiState{

    return CompilationItemUiState(
        id = id,
        title = title,
        description = description,
        photo = photoUrl,
        authorId = user.id,
        authorPhoto = user.photo
    )
}
