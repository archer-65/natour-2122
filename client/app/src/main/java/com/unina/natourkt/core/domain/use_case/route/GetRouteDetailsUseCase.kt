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
 * This UseCase is used to retrieve post details
 * @see [RouteRepository]
 */
class GetRouteDetailsUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(routeId: Long): Flow<DataState<Route>> = flow {
        emit(DataState.Loading())
        Log.i(Constants.ROUTE_MODEL, "Getting route details for post with ID: ${routeId}...")

        val routeDetails = routeRepository.getRouteById(routeId)
        emit(routeDetails)
    }
}