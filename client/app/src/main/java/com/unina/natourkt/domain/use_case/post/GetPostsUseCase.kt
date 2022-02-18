package com.unina.natourkt.domain.use_case.post

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.model.post.Post
import com.unina.natourkt.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

import java.lang.Exception
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(): Flow<PagingData<Post>> =
        postRepository.getPosts()
}