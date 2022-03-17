package com.unina.natourkt.core.domain.model

data class PostCreation(
    val description: String,
    val photos: List<String>,
    val author: User?,
    val taggedRoute: RouteTitle,
)