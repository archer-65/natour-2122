package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for [UserDto]
 */
interface UserApi {

    @GET("/users/search")
    suspend fun getUserByUUID(@Query("uuid") cognitoId: String): UserDto
}