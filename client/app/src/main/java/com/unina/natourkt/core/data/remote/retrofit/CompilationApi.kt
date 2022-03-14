package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.CompilationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CompilationApi {

    @GET("/compilations/search_page")
    suspend fun getCompilationsByUser(
        @Query("user_id") userId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ) : List<CompilationDto>
}