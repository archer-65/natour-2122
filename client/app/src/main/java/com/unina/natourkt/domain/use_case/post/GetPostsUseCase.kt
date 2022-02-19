package com.unina.natourkt.domain.use_case.post

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {

    operator fun invoke(): Flow<PagingData<Post>> =
        postRepository.getPosts()
}