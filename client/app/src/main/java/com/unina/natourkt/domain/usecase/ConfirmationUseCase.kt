package com.unina.natourkt.domain.usecase

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.Constants.AMPLIFY
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.repository.AuthRepositoryImpl
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConfirmationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(
        username: String,
        code: String
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())
            val isSignUpComplete = authRepository.confirmRegistration(username, code)

            if (isSignUpComplete) {
                emit(DataState.Success(isSignUpComplete))
            } else {
                emit(DataState.Error("Something went wrong, retry."))
            }
        } catch (e: AuthException) {
            Log.e(AMPLIFY, "Error is ", e)
            val message: String = when (e) {
                is AuthException.CodeDeliveryFailureException -> "Error in delivering the confirmation code, require another code."

                is AuthException.CodeMismatchException -> "Confirmation code is not correct, retry."

                is AuthException.CodeExpiredException -> "Confirmation code has expired"

                else -> e.localizedMessage ?: "Unknown error, retry later."
            }

            emit(DataState.Error(message))
        }
    }
}