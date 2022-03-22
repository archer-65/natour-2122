package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import com.unina.natourkt.core.util.Difficulty
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class RouteDetailsUi(
    val id: Long,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val duration: Double,
    val disabilityFriendly: Boolean,
    val creationDate: LocalDateTime,
    val modifiedDate: LocalDateTime?,
    val isReported: Boolean,
    val photos: List<String>,
    val stops: List<RouteStopUi>,
    val author: UserUi,
) : Parcelable {

    suspend fun convertKeys(execute: suspend (string: String) -> String): RouteDetailsUi {
        val photos = this.photos.map {
            execute(it)
        }

        return this.copy(photos = photos)
    }
}