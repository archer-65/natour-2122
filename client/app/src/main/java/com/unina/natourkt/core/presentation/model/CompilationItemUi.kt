package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompilationItemUi(
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val authorId: Long,
    val authorPhoto: String?,
) : Parcelable {

    suspend fun convertKeys(execute: suspend (string: String) -> String): CompilationItemUi {
        val authorPhoto = this.authorPhoto?.let { execute(it) }
        val photo = execute(this.photo)

        return this.copy(photo = photo, authorPhoto = authorPhoto)
    }
}
