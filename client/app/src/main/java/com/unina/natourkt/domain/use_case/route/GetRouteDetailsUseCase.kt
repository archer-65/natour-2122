package com.unina.natourkt.domain.use_case.route

import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.repository.PostRepository
import com.unina.natourkt.domain.repository.RouteRepository
import com.unina.natourkt.domain.use_case.maps.GetDirectionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRouteDetailsUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(routeId: Long): Flow<DataState<Route>> = flow {
        emit(DataState.Loading())

        val routeDetails = routeRepository.getRouteById(routeId)
        emit(routeDetails)
    }
}