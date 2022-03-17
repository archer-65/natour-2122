package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

interface CompilationRepository {

    fun getPersonalCompilations(userId: Long): Flow<PagingData<Compilation>>
    suspend fun createCompilation(compilation: CompilationCreation): DataState<Unit>
    suspend fun getPersonalCompilationsToAddRoute(userId: Long, routeId: Long): DataState<List<Compilation>>
    suspend fun addRouteToCompilation(compilationId: Long, routeId: Long): DataState<Unit>
}