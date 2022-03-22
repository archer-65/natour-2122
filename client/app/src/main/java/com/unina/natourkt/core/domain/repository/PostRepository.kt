package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.model.PostCreation
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
    suspend fun reportPost(postId: Long): DataState<Unit>
    suspend fun deletePost(postId: Long): DataState<Unit>
}