package com.unina.natourkt.core.domain.use_case.route

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve routes saved in a compilation
 * @see [RouteRepository]
 */
class GetCompilationRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {

    operator fun invoke(compilationId: Long): Flow<PagingData<Route>> {
        Log.i(
            Constants.ROUTE_MODEL,
            "Getting routes for compilation with ID: ${compilationId}..."
        )

        return routeRepository.getCompilationRoutes(compilationId)
    }
}