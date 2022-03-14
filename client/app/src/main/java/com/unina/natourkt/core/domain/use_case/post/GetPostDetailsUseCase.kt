package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import com.unina.natourkt.core.util.Constants.POST_MODEL
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(id: Long): Flow<DataState<Post>> = flow {
        Log.i(POST_MODEL, "Getting post details...")
        emit(DataState.Loading())
        val post = postRepository.getPostDetails(id)

        emit(post)
    }
}