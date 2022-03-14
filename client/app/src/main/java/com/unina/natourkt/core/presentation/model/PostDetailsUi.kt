package com.unina.natourkt.core.presentation.model


data class PostDetailsUi(
    val id: Long,
    val description: String?,
    val photos: List<String>,
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String?,
    val routeId: Long?,
    val routeTitle: String
) {

    suspend fun convertKeys(execute: suspend (string: String) -> String): PostDetailsUi {
        val authorPhoto = this.authorPhoto?.let { execute(it) }
        val photos = this.photos.map {
            execute(it)
        }

        return this.copy(photos = photos, authorPhoto = authorPhoto)
    }
}