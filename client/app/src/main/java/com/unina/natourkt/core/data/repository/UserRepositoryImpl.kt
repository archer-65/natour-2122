package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.data.paging.UsersSource
import com.unina.natourkt.core.data.remote.dto.mapper.DirectionsApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.UserApiMapper
import com.unina.natourkt.core.data.remote.retrofit.MapsApi
import com.unina.natourkt.core.data.remote.retrofit.UserApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.repository.MapsRepository
import com.unina.natourkt.core.domain.repository.PreferencesRepository
import com.unina.natourkt.core.domain.repository.UserRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [UserRepository] works with a [UserApi] Retrofit interface and
 * with [PreferencesRepository] to store the user after updating the data (like profile pic)
 * It also contains mapper for model conversions
 *
 * @see [UserApiMapper]
 */
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val preferencesRepository: PreferencesRepository,
    private val userApiMapper: UserApiMapper
) : UserRepository {

    /**
     * Page size for network requests for this class
     * NOTE: The first page defaults to pageSize * 3!
     */
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    override suspend fun getUserByUUID(uuid: String): User {
        val response = api.getUserByUUID(uuid)
        return userApiMapper.mapToDomain(response)
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

            preferencesRepository.saveUserToPreferences(userToSave)

            return@safeApiCall newUser
        }
}