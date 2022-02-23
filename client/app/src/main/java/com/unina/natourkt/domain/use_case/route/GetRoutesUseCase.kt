package com.unina.natourkt.domain.use_case.route

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.common.Constants.ROUTE_MODEL
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Paginated [Post] from [RouteRepository]
 */
class GetRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    /**
     * Get [Flow] of [PagingData] for [Post] model
     */
    operator fun invoke(): Flow<PagingData<Route>> {
        Log.i(ROUTE_MODEL, "Getting paginated routes...")
        return routeRepository.getRoutes()
    }
}