package com.unina.natourkt.core.domain.use_case.route

import android.util.Log
import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve routes' titles
 * @see [RouteRepository]
 */
class GetRouteTitleUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {

    operator fun invoke(title: String): Flow<DataState<List<RouteTitle>>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.ROUTE_MODEL,
            "Getting routes with title containing: ${title}..."
        )

        val routes = routeRepository.getRoutesTitles(title)
        emit(routes)
    }
}