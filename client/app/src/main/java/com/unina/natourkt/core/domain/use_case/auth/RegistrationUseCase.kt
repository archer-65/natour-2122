package com.unina.natourkt.core.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.Constants.REGISTRATION_STATE
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase provides sign up functionality through username, email and password
 */
class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<DataState<Boolean>> = flow {
        Log.i(REGISTRATION_STATE, "Processing sign up request...")
        emit(DataState.Loading())

        val signUpResult = authRepository.register(username, email, password)
        emit(signUpResult)
    }
}