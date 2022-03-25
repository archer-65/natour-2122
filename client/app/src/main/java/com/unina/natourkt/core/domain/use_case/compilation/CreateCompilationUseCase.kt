package com.unina.natourkt.core.domain.use_case.compilation

import android.util.Log
import androidx.core.net.toUri
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to create a compilation, it also loads images
 * @see [CompilationRepository]
 * @see [UploadFilesUseCase]
 */
class CreateCompilationUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(compilation: CompilationCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.COMPILATION_MODEL,
            "Creating compilation ${compilation.title} with author ${compilation.author?.username}..."
        )

        val compilationWithUploadedFile = uploadPhoto(compilation)
        val result = compilationRepository.createCompilation(compilationWithUploadedFile)

        emit(result)
    }

    /**
     * This function decouples the uploading of photos from the main use case functionality
     */
    private suspend fun uploadPhoto(compilation: CompilationCreation): CompilationCreation {
        val pathDefinition = definePath(compilation)

        val uploadedPhoto = uploadFilesUseCase(pathDefinition, compilation.photo.toUri())
        Log.i(
            Constants.COMPILATION_MODEL,
            "Compilation's photos loaded successfully..."
        )

        return uploadedPhoto?.let { compilation.copy(photo = it) } ?: compilation
    }

    /**
     * This function generates a path for the uploaded image
     */
    private fun definePath(compilation: CompilationCreation): String {
        val prefix = "compilations/${compilation.author?.id}"
        val title = compilation.title.replace(" ", "")
        val path = "${prefix}/${title}/image"
        return path
    }
}