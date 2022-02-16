package com.unina.natourkt.domain.model.post

import com.unina.natourkt.domain.model.User

data class Post(
    val id: Long,
    val description: String,
    val isReported: Boolean = false,
    val photos: List<PostPhoto>,
    val user: User,
)
