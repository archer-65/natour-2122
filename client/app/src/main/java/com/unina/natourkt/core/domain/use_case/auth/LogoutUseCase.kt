package com.unina.natourkt.core.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.Constants.LOGIN_STATE
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase provides the logout functionality
 * @see [AuthRepository]
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): Flow<DataState<Boolean>> = flow {
        Log.i(LOGIN_STATE, "Processing logout request...")
        emit(DataState.Loading())

        val loginResult = authRepository.logout()
        emit(loginResult)
    }
}