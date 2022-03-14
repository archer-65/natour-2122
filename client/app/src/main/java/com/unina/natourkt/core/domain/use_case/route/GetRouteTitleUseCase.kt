package com.unina.natourkt.core.domain.use_case.route

import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.RouteTitle
import com.unina.natourkt.core.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRouteTitleUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    operator fun invoke(title: String): Flow<DataState<List<RouteTitle>>> = flow {
        emit(DataState.Loading())

        val routes = routeRepository.getRouteTitle(title)
        emit(routes)
    }
}