package com.unina.natourkt.domain.repository

import com.unina.natourkt.domain.model.post.Post

interface PostRepository {

    suspend fun getPosts(pageNo: Int): List<Post>
}