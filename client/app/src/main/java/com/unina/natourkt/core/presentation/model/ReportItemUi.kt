package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportItemUi(
    val id: Long,
    val title: String,
    val description: String,
    val authorPhoto: String,
    val reportedRoute: RouteTitleUi,
) : Parcelable {

    suspend fun convertKeys(execute: suspend (string: String) -> String): ReportItemUi {
        val newPreview = execute(this.authorPhoto)
        return this.copy(authorPhoto = newPreview)
    }
}