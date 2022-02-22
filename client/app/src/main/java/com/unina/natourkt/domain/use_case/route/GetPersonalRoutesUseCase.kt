package com.unina.natourkt.domain.use_case.route

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.common.Constants.ROUTE_MODEL
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.repository.RouteRepository
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Paginated [Post] for logged user from [RouteRepository]
 */
class GetPersonalRoutesUseCase @Inject constructor(
    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
    private val routeRepository: RouteRepository,
){

    /**
     * Get [Flow] of [PagingData] for personal [Route]
     */
    suspend operator fun invoke(): Flow<PagingData<Route>> {

        val loggedUser = getUserFromStoreUseCase()

        Log.i(ROUTE_MODEL, "Getting paginated routes for user...")
        return routeRepository.getPersonalRoutes(loggedUser!!.id)
    }
}