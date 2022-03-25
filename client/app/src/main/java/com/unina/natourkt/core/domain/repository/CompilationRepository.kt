package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for compilation functions repository
 */
interface CompilationRepository {

    /**
     * This function gets all compilations for a certain user
     */
    fun getPersonalCompilations(userId: Long): Flow<PagingData<Compilation>>

    /**
     * This function creates a compilation taking only a [CompilationCreation] model as
     * parameter
     */
    suspend fun createCompilation(compilation: CompilationCreation): DataState<Unit>

    /**
     * This function gets all compilations for a certain user where a given route is not present
     */
    suspend fun getPersonalCompilationsToAddRoute(
        userId: Long,
        routeId: Long
    ): DataState<List<Compilation>>

    /**
     * This function adds a route to a certain compilation
     */
    suspend fun addRouteToCompilation(compilationId: Long, routeId: Long): DataState<Unit>

    /**
     * This function removes a route from a certain compilation
     */
    suspend fun removeRouteFromCompilation(compilationId: Long, routeId: Long): DataState<Unit>

    /**
     * This function delete a compilation
     */
    suspend fun removeCompilation(compilationId: Long): DataState<Unit>
}