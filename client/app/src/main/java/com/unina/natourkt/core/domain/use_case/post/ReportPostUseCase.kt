package com.unina.natourkt.core.domain.use_case.post

import android.util.Log
import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * This UseCase is used to report a post
 * @see [PostRepository]
 */
class ReportPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(postId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.POST_MODEL,
            "Reporting post with ID: ${postId}"
        )

        val result = postRepository.reportPost(postId)
        emit(result)
    }
}