package com.unina.natourkt.domain.use_case.route

import androidx.core.net.toUri
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.model.route.Route
import com.unina.natourkt.domain.repository.RouteRepository
import com.unina.natourkt.domain.repository.StorageRepository
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import com.unina.natourkt.domain.use_case.storage.UploadFilesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
    private val uploadFilesUseCase: UploadFilesUseCase,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(route: Route): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            val routeWithUser = prepareRouteWithUser(route)
            val preparedRoute = prepareRouteForUpload(routeWithUser)
            routeRepository.createRoute(preparedRoute)

            emit(DataState.Success(true))
        } catch (e: Exception) {
            emit(DataState.Error(ErrorHandler.handleException(e)))
        }
    }

    private suspend fun prepareRouteWithUser(route: Route) =
        route.copy(user = getUserFromStoreUseCase())

    private suspend fun prepareRouteForUpload(route: Route) =
        route.copy(photos = prepareUpload(route))

    private suspend fun prepareUpload(route: Route) =
        route.photos.mapIndexed { index, value ->
            uploadFilesUseCase(prefixCreation(index, route), value.toUri())
        }.filterNotNull()

    private fun prefixCreation(index: Int, route: Route): String {
        val prefix = "routes/${route.user?.id}"
        val title = route.title.replace(" ", "")
        val path = "${prefix}/${title}/image${index}"
        return path
    }
}