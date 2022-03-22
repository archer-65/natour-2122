package com.unina.natourkt.core.domain.use_case.route

import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(route: Route): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val result = routeRepository.updateRoute(route)
        emit(result)
    }
}