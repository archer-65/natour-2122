package com.unina.natourkt.data.repository

import com.unina.natourkt.data.remote.PostRetrofitDataSource
import com.unina.natourkt.data.remote.dto.toPost
import com.unina.natourkt.domain.model.post.Post
import com.unina.natourkt.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val retrofitDataSource: PostRetrofitDataSource
): PostRepository {

    override suspend fun getPosts(pageNo: Int): List<Post> {
        return retrofitDataSource.getPosts(pageNo).map { post -> post.toPost()  }
    }
}