package com.unina.natourkt.domain.use_case.route

import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.RouteTitle
import com.unina.natourkt.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRouteTitleUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    operator fun invoke (title: String): Flow<DataState<List<RouteTitle>>> = flow{
        val routes = routeRepository.getRouteTitle(title)
        emit(routes)
    }
}