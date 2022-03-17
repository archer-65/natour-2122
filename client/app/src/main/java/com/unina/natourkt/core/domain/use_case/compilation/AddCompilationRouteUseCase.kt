package com.unina.natourkt.core.domain.use_case.compilation

import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddCompilationRouteUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
) {

    operator fun invoke(compilationId: Long, routeId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val result = compilationRepository.addRouteToCompilation(compilationId, routeId)
        emit(result)
    }
}