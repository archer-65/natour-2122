package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve route's tagged posts
 * @see [PostRepository]
 * @see [GetUserDataUseCase]
 */
class GetTaggedPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(routeId: Long): Flow<PagingData<Post>> {
        Log.i(
            Constants.POST_MODEL,
            "Getting posts with tagged route ${routeId}..."
        )

        return postRepository.getTaggedPosts(routeId)
    }
}