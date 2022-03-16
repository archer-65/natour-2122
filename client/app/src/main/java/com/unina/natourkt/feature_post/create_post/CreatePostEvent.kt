package com.unina.natourkt.feature_post.create_post

import android.net.Uri

sealed class CreatePostEvent {
    // GENERAL
    object Upload : CreatePostEvent()

    // INFO
    data class EnteredQuery(val query: String) : CreatePostEvent()
    data class EnteredRoute(val title: String) : CreatePostEvent()
    data class EnteredDescription(val description: String) : CreatePostEvent()

    // PHOTOS
    data class InsertedPhotos(val photos: List<Uri>) : CreatePostEvent()
    data class RemovePhoto(val position: Int) : CreatePostEvent()
}