package com.unina.natourkt.core.domain.use_case.user

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.repository.UserRepository
import com.unina.natourkt.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(query: String, loggedUserId: Long): Flow<PagingData<User>> {
        Log.i(Constants.ROUTE_MODEL, "Getting paginated users...")
        return userRepository.getUsers(query, loggedUserId)
    }
}