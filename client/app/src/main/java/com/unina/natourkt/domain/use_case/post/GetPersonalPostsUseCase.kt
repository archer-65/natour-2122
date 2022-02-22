package com.unina.natourkt.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.common.Constants.POST_MODEL
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.repository.PostRepository
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Paginated [Post] for logged user from [PostRepository]
 */
class GetPersonalPostsUseCase @Inject constructor(
    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
    private val postRepository: PostRepository,
){

    /**
     * Get [Flow] of [PagingData] for personal [Post]
     */
    suspend operator fun invoke(): Flow<PagingData<Post>> {

        val loggedUser = getUserFromStoreUseCase()

        Log.i(POST_MODEL, "Getting paginated posts for user...")
        return postRepository.getPersonalPosts(loggedUser!!.id)
    }
}