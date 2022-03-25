package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.util.Constants.POST_MODEL
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve personal posts
 * @see [PostRepository]
 * @see [GetUserDataUseCase]
 */
class GetPersonalPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val getUserDataUseCase: GetUserDataUseCase,
) {

    suspend operator fun invoke(): Flow<PagingData<Post>> {
        val loggedUser = getUserDataUseCase()
        Log.i(
            POST_MODEL,
            "Getting posts for user: ${loggedUser?.username}..."
        )

        return postRepository.getPersonalPosts(loggedUser!!.id)
    }
}