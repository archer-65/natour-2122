package com.unina.natourkt.core.domain.use_case.route

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.util.Constants.ROUTE_MODEL
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve routes saved in a compilation
 * @see [RouteRepository]
 */
class GetRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(): Flow<PagingData<Route>> {
        Log.i(
            ROUTE_MODEL,
            "Getting routes..."
        )

        return routeRepository.getRoutes()
    }
}