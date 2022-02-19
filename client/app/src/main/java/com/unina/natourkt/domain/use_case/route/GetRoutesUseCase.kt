package com.unina.natourkt.domain.use_case.route

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.Route
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
) {

    operator fun invoke(): Flow<PagingData<Route>> =
        routeRepository.getRoutes()
}