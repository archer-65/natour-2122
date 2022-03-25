package com.unina.natourkt.core.domain.use_case.route

import android.util.Log
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to update a route
 * @see [RouteRepository]
 */
class UpdateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(route: Route): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.ROUTE_MODEL,
            "Reporting post with ID: ${route.id}"
        )

        val result = routeRepository.updateRoute(route)
        emit(result)
    }
}