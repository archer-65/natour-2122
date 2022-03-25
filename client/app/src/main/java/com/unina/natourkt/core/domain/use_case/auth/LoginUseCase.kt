package com.unina.natourkt.core.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.Constants.LOGIN_STATE
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase provides the classic login way with username and password
 * @see [AuthRepository]
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        Log.i(LOGIN_STATE, "Processing login attempt via username and password...")
        emit(DataState.Loading())

        val loginResult = authRepository.login(username, password)
        emit(loginResult)
    }
}