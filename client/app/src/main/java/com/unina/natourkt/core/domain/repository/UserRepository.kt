package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.data.remote.dto.UserDto
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for user functions Repository
 */
interface UserRepository {

    /**
     * This function is used ONLY for providers storing users with sub (UUID)
     */
    suspend fun getUserByUUID(uuid: String): User

    /**
     * This function gets all the users given query (to research users) and loggedUserId (excludes user from research)
     */
    fun getUsers(query: String, loggedUserId: Long): Flow<PagingData<User>>

    /**
     * This function updates a certain [User], given a modified object of the original one
     */
    suspend fun updateUser(user: User): DataState<User>
}