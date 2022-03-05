package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.common.Constants.REGISTRATION_STATE
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of [AuthRepository] to register an user
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