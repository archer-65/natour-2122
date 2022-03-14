package com.unina.natourkt.core.domain.use_case.route

import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.RouteRepository
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