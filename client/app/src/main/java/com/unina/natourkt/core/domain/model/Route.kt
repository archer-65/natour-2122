package com.unina.natourkt.core.domain.model.route

import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.core.domain.model.RouteStop
import com.unina.natourkt.core.domain.model.User
import java.time.LocalDateTime

/**
 * Route model (to improve)
 */
data class Route(
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
    val stops: List<RouteStop>,
    val author: User,
)