package com.unina.natourkt.data.repository

import com.unina.natourkt.data.remote.retrofit.UserRetrofitDataSource
import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val retrofitDataSource: UserRetrofitDataSource
): UserRepository {

    override suspend fun getUserByCognitoId(cognitoId: String): UserDto {
        return retrofitDataSource.getUserByUUID(cognitoId);
    }
}