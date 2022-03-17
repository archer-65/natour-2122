package com.unina.natourkt.core.data.repository

import com.unina.natourkt.core.data.remote.retrofit.UserApi
import com.unina.natourkt.core.data.remote.dto.UserDto
import com.unina.natourkt.core.domain.repository.UserRepository
import javax.inject.Inject

/**
 * This implementation of [UserRepository] contains [User] related functions for incoming
 * responses from [UserApi]
 */
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
): UserRepository {

    /**
     * Only gets User by Cognito ID!
     */
    override suspend fun getUserByCognitoId(cognitoId: String): UserDto {
        return api.getUserByUUID(cognitoId)
    }
}