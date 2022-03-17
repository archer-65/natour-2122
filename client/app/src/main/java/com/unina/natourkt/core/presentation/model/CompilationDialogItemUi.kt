package com.unina.natourkt.core.presentation.model

data class CompilationDialogItemUi(
    val id: Long,
    val title: String,
    val photo: String,
) {

    suspend fun convertKeys(execute: suspend (string: String) -> String): CompilationDialogItemUi {
        val photo = execute(this.photo)

        return this.copy(photo = photo)
    }
}
