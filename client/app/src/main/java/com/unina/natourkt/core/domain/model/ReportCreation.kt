package com.unina.natourkt.core.domain.model

data class ReportCreation(
    val title: String,
    val description: String,
    val author: User?,
    val reportedRouteId: Long
)