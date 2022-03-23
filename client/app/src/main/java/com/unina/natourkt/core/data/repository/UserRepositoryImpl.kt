package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.data.paging.RoutesSource
import com.unina.natourkt.core.data.paging.UsersSource
import com.unina.natourkt.core.data.remote.retrofit.UserApi
import com.unina.natourkt.core.data.remote.dto.UserDto
import com.unina.natourkt.core.data.remote.dto.mapper.UserApiMapper
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.model.route.Route
import com.unina.natourkt.core.domain.repository.PreferencesRepository
import com.unina.natourkt.core.domain.repository.UserRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [UserRepository] contains [User] related functions for incoming
 * responses from [UserApi]
 */
class UserRepositoryImpl @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val api: UserApi,
    private val userApiMapper: UserApiMapper
) : UserRepository {

    /**
     * Page size for network requests for this class
     * NOTE: The first page defaults to pageSize * 3!
     */
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    /**
     * Only gets User by Cognito ID!
     */
    override suspend fun getUserByCognitoId(cognitoId: String): UserDto {
        return api.getUserByUUID(cognitoId)
    }

    override fun getUsers(query: String, loggedUserId: Long): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersSource(api, userApiMapper, query, loggedUserId) }
        ).flow
    }

    override suspend fun updateUser(user: User): DataState<User> =
        safeApiCall(IO) {
            val userRequest = userApiMapper.mapToDto(user)
            val response = api.updateUser(userRequest.id, userRequest)

            val newUser = userApiMapper.mapToDomain(response)
            val userToSave = newUser.copy(isAdmin = user.isAdmin)

            preferencesRepository.saveUserToDataStore(userToSave)

            return@safeApiCall newUser
        }
}