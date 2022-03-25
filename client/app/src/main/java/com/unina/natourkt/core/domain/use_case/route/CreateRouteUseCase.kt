package com.unina.natourkt.core.domain.use_case.route

import android.util.Log
import androidx.core.net.toUri
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.repository.RouteRepository
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to create a post, it also loads images
 * @see [RouteRepository]
 * @see [UploadFilesUseCase]
 */
class CreateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(route: RouteCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.ROUTE_MODEL,
            "Creating route ${route.title} with author ${route.author?.username}..."
        )

        val routeWithUploadedFiles = uploadPhotos(route)
        val result = routeRepository.createRoute(routeWithUploadedFiles)

        emit(result)
    }

    /**
     * This function decouples the uploading of photos from the main use case functionality
     */
    private suspend fun uploadPhotos(route: RouteCreation): RouteCreation {
        val uploadedPhotos = route.photos.mapIndexed { index, value ->
            // First define a path
            val pathDefinition = definePath(index, route)
            // Upload photo with path
            uploadFilesUseCase(pathDefinition, value.toUri())
        }.filterNotNull()

        Log.i(
            Constants.ROUTE_MODEL,
            "Route's photos loaded successfully..."
        )

        return route.copy(photos = uploadedPhotos)
    }

    /**
     * This function generates a path for the uploaded image
     */
    private fun definePath(index: Int, route: RouteCreation): String {
        val prefix = "routes/${route.author?.id}"
        val title = route.title.replace(" ", "")
        val path = "${prefix}/${title}/image${index}"
        return path
    }
}