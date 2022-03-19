package com.unina.natourkt.core.domain.model

data class Report(
    val id: Long,
    val title: String,
    val description: String,
    val author: User?,
    val reportedRoute: RouteTitle
)