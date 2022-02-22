package com.unina.natourkt.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.Compilation
import kotlinx.coroutines.flow.Flow

interface CompilationRepository {

    fun getPersonalCompilations(userId: Long): Flow<PagingData<Compilation>>
}