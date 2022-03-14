package com.unina.natourkt.core.domain.model

import com.unina.natourkt.core.data.remote.dto.PostCreationDto

data class PostCreation(
    val description: String,
    val photos: List<String>,
    val author: User? = null,
    val taggedRoute: RouteTitle,
)