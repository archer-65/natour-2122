package com.unina.natourkt.domain.repository

import com.unina.natourkt.data.remote.dto.UserDto

interface UserRepository {

    suspend fun getUserByCognitoId(cognitoId: String): UserDto
}