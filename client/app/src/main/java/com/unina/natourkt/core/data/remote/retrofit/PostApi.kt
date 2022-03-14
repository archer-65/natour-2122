package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.PostCreationDto
import com.unina.natourkt.core.data.remote.dto.post.PostDto
import retrofit2.http.*

/**
 * Retrofit interface for [PostDto]
 */
interface PostApi {

    /**
     * Classic Get all posts (paginated)
     */
    @GET("/posts")
    suspend fun getPosts(
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<PostDto>

    /**
     * Get all routes by user id (paginated)
     */
    @GET("/posts/search_page")
    suspend fun getPostsByUser(
        @Query("user_id") userId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<PostDto>


    @GET("/posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Long
    ): PostDto

    @GET("/posts/tag")
    suspend fun getTaggedPosts(
        @Query("route_id") userId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<PostDto>

    @POST("/posts/add")
    suspend fun createRoute(
        @Body postDto: PostCreationDto
    )
}


