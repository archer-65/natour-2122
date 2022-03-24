package com.unina.natourkt.feature_post.post_details

sealed class PostDetailsEvent {
    object ShowChat : PostDetailsEvent()
    object ResetChat : PostDetailsEvent()
}