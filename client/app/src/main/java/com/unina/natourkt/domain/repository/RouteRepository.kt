package com.unina.natourkt.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    fun getRoutes(): Flow<PagingData<Route>>
}