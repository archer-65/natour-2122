package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.data.remote.dto.UserDto
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.model.route.Route
import kotlinx.coroutines.flow.Flow

/**
 * Interface for User Repository
 */
interface UserRepository {

    /**
     * This function is used ONLY for Cognito
     */
    suspend fun getUserByCognitoId(cognitoId: String): UserDto

    fun getUsers(query: String, loggedUserId: Long): Flow<PagingData<User>>
}