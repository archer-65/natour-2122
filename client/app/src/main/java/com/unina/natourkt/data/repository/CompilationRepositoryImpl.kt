package com.unina.natourkt.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.data.remote.paging.PersonalCompilationPagingSource
import com.unina.natourkt.data.remote.retrofit.CompilationRetrofitDataSource
import com.unina.natourkt.domain.model.Compilation
import com.unina.natourkt.domain.repository.CompilationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompilationRepositoryImpl @Inject constructor(
    private val retrofitDataSource: CompilationRetrofitDataSource
): CompilationRepository  {

    companion object{
        const val  NETWORK_PAGE_SIZE = 10
    }

    override fun getPersonalCompilations(userId: Long): Flow<PagingData<Compilation>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory =  { PersonalCompilationPagingSource(retrofitDataSource, userId) }
        ).flow
    }
}