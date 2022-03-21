package com.unina.natourkt.core.domain.use_case.post

import com.unina.natourkt.core.domain.repository.PostRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(postId: Long): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val result = postRepository.reportPost(postId)
        emit(result)
    }
}