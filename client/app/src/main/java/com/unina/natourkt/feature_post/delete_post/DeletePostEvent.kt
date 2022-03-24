package com.unina.natourkt.feature_post.delete_post

sealed class DeletePostEvent {
    // GENERAL
    object OnDelete : DeletePostEvent()
}