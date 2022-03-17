package com.unina.natourkt.core.domain.use_case.compilation

import android.util.Log
import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPersonalCompilationsToAddRoute @Inject constructor(
    private val compilationRepository: CompilationRepository,
) {

    operator fun invoke(userId: Long, routeId: Long): Flow<DataState<List<Compilation>>> = flow {
        emit(DataState.Loading())

        val result = compilationRepository.getPersonalCompilationsToAddRoute(userId, routeId)
        emit(result)
    }
}