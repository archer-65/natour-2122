package com.unina.natourkt.domain.use_case.route

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.Filter
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(filter: Filter): Flow<PagingData<Route>> {
        return routeRepository.getFilteredRoutes(filter)
    }
}