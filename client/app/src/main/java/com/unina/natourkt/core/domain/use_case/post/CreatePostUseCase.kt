package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import androidx.core.net.toUri
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to create a post, it also loads images
 * @see [PostRepository]
 * @see [UploadFilesUseCase]
 */
class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(post: PostCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.POST_MODEL,
            "Creating post for route ${post.taggedRoute.title} with author ${post.author?.username}..."
        )

        val postWithUploadedFiles = uploadPhotos(post)
        val createdPost = postRepository.createPost(postWithUploadedFiles)

        emit(createdPost)
    }

    /**
     * This function decouples the uploading of photos from the main use case functionality
     */
    private suspend fun uploadPhotos(post: PostCreation): PostCreation {
        val uploadedPhotos = post.photos.mapIndexed { index, value ->
            // First define a path
            val pathDefinition = definePath(index, post)
            // Upload photo with path
            uploadFilesUseCase(pathDefinition, value.toUri())
        }.filterNotNull()

        Log.i(
            Constants.COMPILATION_MODEL,
            "Post's photos loaded successfully..."
        )

        return post.copy(photos = uploadedPhotos)
    }

    /**
     * This function generates a path for the uploaded image
     */
    private fun definePath(index: Int, post: PostCreation): String {
        val prefix = "posts/${post.author?.id}"
        val title = post.taggedRoute.title.replace(" ", "")
        val path = "${prefix}/${title}/image${index}"
        return path
    }
}