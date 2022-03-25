package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.util.Constants.POST_MODEL
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve recent posts
 * @see [PostRepository]
 */
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    /**
     * Get [Flow] of [PagingData] for [Post] model
     */
    operator fun invoke(): Flow<PagingData<Post>> {
        Log.i(
            POST_MODEL,
            "Getting recent posts..."
        )
        return postRepository.getPosts()
    }
}