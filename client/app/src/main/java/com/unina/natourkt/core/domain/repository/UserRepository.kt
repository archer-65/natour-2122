package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.data.remote.dto.UserDto

/**
 * Interface for User Repository
 */
interface UserRepository {

    /**
     * This function is used ONLY for Cognito
     */
    suspend fun getUserByCognitoId(cognitoId: String): UserDto
}