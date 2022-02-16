package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class ResendConfirmationCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(username: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            val isCodeSent = authRepository.resendCode(username)

            if (isCodeSent) {
                // If the code is resent successfully emit
                emit(DataState.Success(isCodeSent))
            } else {
                emit(DataState.Error(DataState.CustomMessages.SomethingWentWrong("Unknown Error resending code")))
            }
        } catch (e: Exception) {
            Log.e("RESEND CODE STATE", e.localizedMessage ?: "Resend failed due to exception", e)
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}