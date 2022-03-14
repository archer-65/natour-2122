package com.unina.natourkt.domain.model

import com.unina.natourkt.presentation.base.model.CompilationItemUiState

data class Compilation(
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val author: User
)

fun Compilation.toUi(): CompilationItemUiState{
    return CompilationItemUiState(
        id = id,
        title = title,
        description = description,
        photo = photo,
        authorId = author.id,
        authorPhoto = author.profilePhoto
    )
}
