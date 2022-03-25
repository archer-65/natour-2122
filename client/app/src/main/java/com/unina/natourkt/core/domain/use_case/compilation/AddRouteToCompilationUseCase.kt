package com.unina.natourkt.core.domain.use_case.compilation

import android.util.Log
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to add a certain route to the compilation chosen by the user
 * @see [CompilationRepository]
 */
class AddRouteToCompilationUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
) {

    operator fun invoke(compilationId: Long, routeId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.COMPILATION_MODEL,
            "Saving route with ID: ${routeId} into compilation with ID: ${compilationId}..."
        )

        val result = compilationRepository.addRouteToCompilation(compilationId, routeId)
        emit(result)
    }
}