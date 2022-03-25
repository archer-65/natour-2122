package com.unina.natourkt.core.domain.use_case.compilation

import android.util.Log
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to delete a compilation
 * @see [CompilationRepository]
 */
class DeleteCompilationUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
) {

    operator fun invoke(compilationId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.COMPILATION_MODEL,
            "Deleting compilation ${compilationId}..."
        )

        val result = compilationRepository.removeCompilation(compilationId)
        emit(result)
    }
}