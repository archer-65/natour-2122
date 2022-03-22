package com.unina.natourkt.feature_route.update_route

sealed class UpdateRouteEvent {
    object Upload : UpdateRouteEvent()

    data class EnteredDescription(val description: String) : UpdateRouteEvent()
}