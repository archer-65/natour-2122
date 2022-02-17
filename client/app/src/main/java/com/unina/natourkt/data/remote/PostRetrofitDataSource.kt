package com.unina.natourkt.data.remote

import com.unina.natourkt.data.remote.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PostRetrofitDataSource {

    @GET("/posts")
    suspend fun getPosts(@Query("pageNo") pageNo: Int): List<PostDto>
}