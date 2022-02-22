package com.unina.natourkt.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Interface for post related functions
 */
interface PostRepository {

    /**
     * Get paginated posts
     */
    fun getPosts(): Flow<PagingData<Post>>

    /**
     * Get paginated posts for logged user
     */
    fun getPersonalPosts(userId: Long): Flow<PagingData<Post>>
}