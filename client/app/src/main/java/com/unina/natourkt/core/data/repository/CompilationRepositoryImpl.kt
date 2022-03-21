package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.data.paging.PersonalCompilationSource
import com.unina.natourkt.core.data.remote.dto.mapper.CompilationApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.CompilationCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.CompilationApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompilationRepositoryImpl @Inject constructor(
    private val api: CompilationApi,
    private val compilationApiMapper: CompilationApiMapper,
    private val compilationCreationApiMapper: CompilationCreationApiMapper
) : CompilationRepository {

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    override fun getPersonalCompilations(userId: Long): Flow<PagingData<Compilation>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PersonalCompilationSource(
                    api,
                    compilationApiMapper,
                    userId
                )
            }
        ).flow
    }

    override suspend fun getPersonalCompilationsToAddRoute(
        userId: Long,
        routeId: Long
    ): DataState<List<Compilation>> {
        return safeApiCall(IO) {
            val compilationResponse = api.getCompilationsByUserAndRouteNotPresent(userId, routeId)
            compilationResponse.map { compilationApiMapper.mapToDomain(it) }
        }
    }

    override suspend fun createCompilation(compilation: CompilationCreation): DataState<Unit> =
        safeApiCall(IO) {
            val compilationRequest = compilationCreationApiMapper.mapToDto(compilation)
            api.createCompilation(compilationRequest)
        }

    override suspend fun addRouteToCompilation(
        compilationId: Long,
        routeId: Long
    ): DataState<Unit> =
        safeApiCall(IO) {
            api.addRouteToCompilation(compilationId, routeId)
        }

    override suspend fun removeRouteFromCompilation(
        compilationId: Long,
        routeId: Long
    ): DataState<Unit> =
        safeApiCall(IO) {
            api.removeRouteFromCompilation(compilationId, routeId)
        }

    override suspend fun removeCompilation(
        compilationId: Long,
    ): DataState<Unit> =
        safeApiCall(IO) {
            api.removeCompilation(compilationId)
        }
}