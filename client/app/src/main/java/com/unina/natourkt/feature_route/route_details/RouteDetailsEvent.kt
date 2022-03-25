package com.unina.natourkt.feature_route.route_details

sealed class RouteDetailsEvent {
    object ShowChat : RouteDetailsEvent()
    object ResetChat : RouteDetailsEvent()
    object ClickPost: RouteDetailsEvent()
}