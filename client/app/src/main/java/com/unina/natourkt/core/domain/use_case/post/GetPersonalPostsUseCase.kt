package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.util.Constants.POST_MODEL
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Paginated [Post] for logged user from [PostRepository]
 */
class GetPersonalPostsUseCase @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val postRepository: PostRepository,
){

    /**
     * Get [Flow] of [PagingData] for personal [Post]
     */
    suspend operator fun invoke(): Flow<PagingData<Post>> {

        val loggedUser = getUserDataUseCase()

        Log.i(POST_MODEL, "Getting paginated posts for user...")
        return postRepository.getPersonalPosts(loggedUser!!.id)
    }
}