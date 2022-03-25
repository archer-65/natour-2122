package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Post
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for post related functions
 */
interface PostRepository {

    /**
     * This function gets all recent posts
     */
    fun getPosts(): Flow<PagingData<Post>>

    /**
     * This function gets all posts for logged user
     */
    fun getPersonalPosts(userId: Long): Flow<PagingData<Post>>

    /**
     * This function gets posts related to given route
     */
    fun getTaggedPosts(routeId: Long): Flow<PagingData<Post>>

    /**
     * This functions gets post details given its id
     */
    suspend fun getPostDetails(id: Long): DataState<Post>

    /**
     * This function creates a compilation taking only a [PostCreation] model as parameter
     */
    suspend fun createPost(post: PostCreation): DataState<Unit>

    /**
     * This function reports a certain post
     */
    suspend fun reportPost(postId: Long): DataState<Unit>

    /**
     * This function deletes a post
     */
    suspend fun deletePost(postId: Long): DataState<Unit>
}