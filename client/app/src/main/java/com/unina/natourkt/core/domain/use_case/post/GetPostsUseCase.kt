package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.util.Constants.POST_MODEL
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Paginated [Post] from [PostRepository]
 */
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository,
) {

    /**
     * Get [Flow] of [PagingData] for [Post] model
     */
    operator fun invoke(): Flow<PagingData<Post>> {
        Log.i(POST_MODEL, "Getting paginated posts...")
        return postRepository.getPosts()
    }
}