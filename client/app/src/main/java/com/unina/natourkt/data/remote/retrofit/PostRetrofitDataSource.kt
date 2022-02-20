package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.post.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for [PostDto]
 */
interface PostRetrofitDataSource {

    /**
     * Classic Get all posts (paginated)
     */
    @GET("/posts")
    suspend fun getPosts(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ): List<PostDto>
}