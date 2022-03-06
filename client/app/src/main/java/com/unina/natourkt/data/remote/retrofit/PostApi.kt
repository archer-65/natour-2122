package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.PostCreationDto
import com.unina.natourkt.data.remote.dto.post.PostDto
import com.unina.natourkt.data.remote.dto.route.RouteDto
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
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ): List<PostDto>

    /**
     * Get all routes by user id (paginated)
     */
    @GET("/posts/search_page")
    suspend fun getPostsByUser(
        @Query("userId") userId: Long,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ): List<PostDto>


    @GET("/posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Long
    ): PostDto

    @POST("/posts/add")
    suspend fun createRoute(
        @Body postDto: PostCreationDto
    )
}


