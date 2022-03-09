package com.unina.natourkt.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.Post
import com.unina.natourkt.domain.model.PostCreation
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

    fun getTaggedPosts(routeId: Long): Flow<PagingData<Post>>

    suspend fun getPostDetails(id: Long) : DataState<Post>

    suspend fun createPost(post: PostCreation): DataState<Unit>
}