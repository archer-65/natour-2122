package com.unina.natourkt.domain.use_case.post

import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPostDetailsUseCase @Inject constructor(
    private val postRepository: PostRepository,
){
    operator fun invoke(id: Long): Flow<DataState<Post>> = flow {
        try {
            emit(DataState.Loading())
            val post = postRepository.getPostDetails(id)
            emit(DataState.Success(post))
        } catch (e: Exception) {
            emit(DataState.Error(ErrorHandler.handleException(e)))
        }
    }

}