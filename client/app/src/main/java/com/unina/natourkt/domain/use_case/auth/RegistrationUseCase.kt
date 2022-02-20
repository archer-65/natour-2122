package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.common.Constants
import com.unina.natourkt.common.Constants.REGISTRATION_STATE
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of [AuthRepository] to register a user
 */
class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    /**
     * Sign up user
     */
    operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            Log.i(REGISTRATION_STATE, "Processing sign up request...")

            val isSignUpComplete = authRepository.register(username, email, password)
            if (isSignUpComplete) {
                // If sign up is successful emit true
                Log.i(REGISTRATION_STATE, "Account creation successful!")
                emit(DataState.Success(isSignUpComplete))
            } else {
                Log.e(REGISTRATION_STATE, "Whoops, something went wrong with sign up, retry!")
                emit(DataState.Error(DataState.CustomMessages.AuthGeneric))
            }
        } catch (e: Exception) {
            Log.e(REGISTRATION_STATE, e.localizedMessage ?: "Sign up failed", e)
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}