package com.unina.natourkt.domain.use_case.route

import com.unina.natourkt.domain.repository.RouteRepository
import javax.inject.Inject

class GetCompilationRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {

    operator fun invoke(compilationId: Long) = routeRepository.getCompilationRoutes(compilationId)
}