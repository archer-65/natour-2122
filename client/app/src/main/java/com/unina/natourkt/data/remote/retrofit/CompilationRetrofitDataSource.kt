package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.CompilationDto
import com.unina.natourkt.data.remote.dto.post.PostDto
import com.unina.natourkt.domain.model.Compilation
import retrofit2.http.GET
import retrofit2.http.Query

interface CompilationRetrofitDataSource {

    @GET("/compilations")
    suspend fun getCompilationsByUser(
        @Query("userId") userId: Long,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ) : List<CompilationDto>
}