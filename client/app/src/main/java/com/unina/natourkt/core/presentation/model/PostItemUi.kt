package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the data class of a post (home screen) element
 */
@Parcelize
data class PostItemUi(
    val id: Long,
    val description: String,
    val photos: List<String> = listOf(),
    val authorId: Long,
    val authorUsername: String,
    val authorPhoto: String,
    val routeId: Long,
    val routeTitle: String,
) : Parcelable {

    suspend fun convertKeys(execute: suspend (string: String) -> String): PostItemUi {
        val authorPhoto = this.authorPhoto.let { execute(it) }
        val photos = this.photos.map {
            execute(it)
        }

        return this.copy(photos = photos, authorPhoto = authorPhoto)
    }
}


