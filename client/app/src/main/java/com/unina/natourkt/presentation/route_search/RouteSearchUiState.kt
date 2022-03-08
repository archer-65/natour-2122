package com.unina.natourkt.presentation.route_search

import com.unina.natourkt.domain.model.Filter

data class RouteSearchUiState(
    val query: String = ""
)

fun RouteSearchUiState.toFilter(): Filter {
    return Filter(
        query = query
    )
}