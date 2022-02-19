package com.unina.natourkt.data.remote.retrofit

import com.unina.natourkt.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for [User]
 */
interface UserRetrofitDataSource {

    @GET("/users/search")
    suspend fun getUserByUUID(@Query("uuid") cognitoId: String): UserDto
}