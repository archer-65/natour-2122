package com.unina.natourkt.core.domain.use_case.route

import android.util.Log
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to delete a route
 * @see [RouteRepository]
 */
class DeleteRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(routeId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.ROUTE_MODEL,
            "Deleting post with ID: ${routeId}"
        )


        val result = routeRepository.deleteRoute(routeId)
        emit(result)
    }
}