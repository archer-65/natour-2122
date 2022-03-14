package com.unina.natourkt.core.domain.model

/**
 * Post model (to improve)
 */
data class Post(
    val id: Long,
    val description: String,
    val isReported: Boolean,
    val photos: List<String>,
    val author: User,
    val taggedRoute: RouteTitle,
)