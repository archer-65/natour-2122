package com.unina.natourkt.core.domain.use_case.compilation

import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCompilationUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
) {

    operator fun invoke(compilationId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val result = compilationRepository.removeCompilation(compilationId)
        emit(result)
    }
}