package com.unina.natourkt.domain.repository

import com.unina.natourkt.data.remote.dto.UserDto

/**
 * Interface for User Repository
 */
interface UserRepository {

    /**
     * This function is used ONLY for Cognito
     */
    suspend fun getUserByCognitoId(cognitoId: String): UserDto
}