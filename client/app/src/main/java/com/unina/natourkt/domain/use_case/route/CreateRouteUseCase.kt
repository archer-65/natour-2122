package com.unina.natourkt.domain.use_case.route

import androidx.core.net.toUri
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.RouteCreation
import com.unina.natourkt.domain.repository.RouteRepository
import com.unina.natourkt.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.domain.use_case.storage.UploadFilesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(route: RouteCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val routeWithUser = prepareRouteWithUser(route)
        val preparedRoute = prepareRouteForUpload(routeWithUser)
        val result = routeRepository.createRoute(preparedRoute)

        emit(result)
    }

    private suspend fun prepareRouteWithUser(route: RouteCreation) =
        route.copy(author = getUserDataUseCase())

    private suspend fun prepareRouteForUpload(route: RouteCreation) =
        route.copy(photos = prepareUpload(route))

    private suspend fun prepareUpload(route: RouteCreation) =
        route.photos.mapIndexed { index, value ->
            uploadFilesUseCase(prefixCreation(index, route), value.toUri())
        }.filterNotNull()

    private fun prefixCreation(index: Int, route: RouteCreation): String {
        val prefix = "routes/${route.author?.id}"
        val title = route.title.replace(" ", "")
        val path = "${prefix}/${title}/image${index}"
        return path
    }
}