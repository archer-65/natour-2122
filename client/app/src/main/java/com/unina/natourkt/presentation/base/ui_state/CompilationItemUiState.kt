package com.unina.natourkt.presentation.base.ui_state

data class CompilationItemUiState(
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val authorId: Long,
    val authorPhoto: String?
)

suspend fun CompilationItemUiState.convertKeys(execute: suspend (string: String) -> String): CompilationItemUiState {
    val authorPhoto = this.authorPhoto?.let { execute(it) }
    val photo = execute(this.photo)

    return this.copy(photo = photo, authorPhoto = authorPhoto)
}
