package com.unina.natourkt.data.remote.dto.route

import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.model.route.Route
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * This class represents the response from API for [Route]
 */
data class RouteDto(
    val id: Long,
    val title: String,
    val description: String,
    val avgDifficulty: Int,
    val avgDuration: Double,
    val disabledFriendly: Boolean,
    val creationDate: String,
    val modifiedDate: String,
    val photos: List<RoutePhotoDto>,
    val stops: List<RouteStopDto>,
    val user: UserDto,
)

/**
 * Function to map [RouteDto] to [Route]
 */
fun RouteDto.toRoute(): Route {
    return Route(
        id = id,
        title = title,
        description = description,
        avgDifficulty = avgDifficulty,
        avgDuration = avgDuration,
        disabledFriendly = disabledFriendly,
        photos = photos.map { photo -> photo.photoUrl },
        stops = stops.map { stop -> stop.toRouteStop() },
        user = user.toUser()
    )
}