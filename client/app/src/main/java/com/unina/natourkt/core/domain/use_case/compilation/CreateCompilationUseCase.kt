package com.unina.natourkt.core.domain.use_case.compilation

import androidx.core.net.toUri
import com.unina.natourkt.core.domain.model.CompilationCreation
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateCompilationUseCase @Inject constructor(
    private val compilationRepository: CompilationRepository,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(compilation: CompilationCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val compilationWithUploadedFile = uploadPhoto(compilation)
        val result = compilationRepository.createCompilation(compilationWithUploadedFile)

        emit(result)
    }

    private suspend fun uploadPhoto(compilation: CompilationCreation): CompilationCreation {
        val pathDefinition = definePath(compilation)
        val uploadedPhoto = uploadFilesUseCase(pathDefinition, compilation.photo.toUri())

        return uploadedPhoto?.let { compilation.copy(photo = it) } ?: compilation
    }

    private fun definePath(compilation: CompilationCreation): String {
        val prefix = "compilations/${compilation.author?.id}"
        val title = compilation.title.replace(" ", "")
        val path = "${prefix}/${title}/image"
        return path
    }
}