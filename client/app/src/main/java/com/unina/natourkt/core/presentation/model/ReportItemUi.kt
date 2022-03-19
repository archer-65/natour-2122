package com.unina.natourkt.core.presentation.model

data class ReportItemUi(
    val id: Long,
    val title: String,
    val description: String,
    val authorPhoto: String,
    val reportedRoute: RouteTitleUi,
) {

    suspend fun convertKeys(execute: suspend (string: String) -> String): ReportItemUi {
        val newPreview = execute(this.authorPhoto)
        return this.copy(authorPhoto = newPreview)
    }
}