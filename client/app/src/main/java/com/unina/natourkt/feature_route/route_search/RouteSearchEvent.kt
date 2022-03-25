package com.unina.natourkt.feature_route.route_search

import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.core.util.Difficulty

sealed class RouteSearchEvent {
    data class EnteredQuery(val query: String) : RouteSearchEvent()
    data class FilterPlace(val place: Place?) : RouteSearchEvent()
    data class FilterDistance(val distance: Float) : RouteSearchEvent()
    data class FilterDuration(val minDuration: Int?, val maxDuration: Int?) : RouteSearchEvent()
    data class FilterDifficulty(val difficulty: Difficulty) : RouteSearchEvent()
    data class FilterDisability(val disability: Boolean?) : RouteSearchEvent()

    object SearchPlace : RouteSearchEvent()
    object ClickRoute : RouteSearchEvent()
}