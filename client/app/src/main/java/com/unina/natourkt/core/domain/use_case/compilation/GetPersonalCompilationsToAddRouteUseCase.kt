package com.unina.natourkt.core.domain.use_case.compilation

import android.util.Log
import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve personal compilations where a certain route is not present,
 * in this way we can save the route in the chosen compilation avoiding duplicates
 * @see [CompilationRepository]
 */
class GetPersonalCompilationsToAddRouteUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
) {

    operator fun invoke(userId: Long, routeId: Long): Flow<DataState<List<Compilation>>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.COMPILATION_MODEL,
            "Retrieving compilations for user with ID: ${userId} where route with ID: ${routeId} is not present ..."
        )

        val result = compilationRepository.getPersonalCompilationsToAddRoute(userId, routeId)
        emit(result)
    }
}