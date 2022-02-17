package com.unina.natourkt.domain.use_case.post

import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.model.post.Post
import com.unina.natourkt.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import java.lang.Exception
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(pageNo: Int): Flow<DataState<List<Post>>> = flow {
        try {
            emit(DataState.Loading())

            val posts = postRepository.getPosts(pageNo)
            emit(DataState.Success(posts))
        } catch (e: Exception) {
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}