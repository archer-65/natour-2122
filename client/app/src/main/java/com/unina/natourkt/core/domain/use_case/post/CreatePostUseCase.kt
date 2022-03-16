package com.unina.natourkt.core.domain.use_case.post

import androidx.core.net.toUri
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(post: PostCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val postWithUploadedFiles = uploadPhotos(post)
        val createdPost = postRepository.createPost(postWithUploadedFiles)

        emit(createdPost)
    }

    private suspend fun uploadPhotos(post: PostCreation): PostCreation {
        val uploadedPhotos = post.photos.mapIndexed { index, value ->
            val pathDefinition = definePath(index, post)
            uploadFilesUseCase(pathDefinition, value.toUri())
        }.filterNotNull()

        return post.copy(photos = uploadedPhotos)
    }

    private fun definePath(index: Int, post: PostCreation): String {
        val prefix = "posts/${post.author?.id}"
        val title = post.taggedRoute.title.replace(" ", "")
        val path = "${prefix}/${title}/image${index}"
        return path
    }
}