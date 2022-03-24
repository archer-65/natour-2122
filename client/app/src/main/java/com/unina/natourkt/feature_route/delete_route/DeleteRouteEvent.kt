package com.unina.natourkt.feature_route.delete_route

sealed class DeleteRouteEvent {
    object OnDelete : DeleteRouteEvent()
}