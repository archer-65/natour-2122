package com.unina.natourkt.core.domain.use_case.route

import androidx.core.net.toUri
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(route: RouteCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val routeWithUploadedFiles = uploadPhotos(route)
        val result = routeRepository.createRoute(routeWithUploadedFiles)

        emit(result)
    }

    private suspend fun uploadPhotos(route: RouteCreation): RouteCreation {
        val uploadedPhotos = route.photos.mapIndexed { index, value ->
            val pathDefinition = definePath(index, route)
            uploadFilesUseCase(pathDefinition, value.toUri())
        }.filterNotNull()

        return route.copy(photos = uploadedPhotos)
    }

    private fun definePath(index: Int, route: RouteCreation): String {
        val prefix = "routes/${route.author?.id}"
        val title = route.title.replace(" ", "")
        val path = "${prefix}/${title}/image${index}"
        return path
    }
}