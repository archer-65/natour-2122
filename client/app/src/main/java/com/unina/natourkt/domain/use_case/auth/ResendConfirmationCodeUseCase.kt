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
 * This UseCase make use of [AuthRepository] to confirm user account
 */
class ResendConfirmationCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    /**
     * Resend confirmation code
     */
    operator fun invoke(username: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            Log.i(REGISTRATION_STATE, "Processing new code request...")

            val isCodeSent = authRepository.resendCode(username)
            if (isCodeSent) {
                // If the code is resent successfully emit
                Log.i(REGISTRATION_STATE, "New code sent!")
                emit(DataState.Success(isCodeSent))
            } else {
                Log.e(REGISTRATION_STATE, "Whoops, something went wrong with code delivery, retry!")
                emit(DataState.Error(DataState.CustomMessage.CodeDelivery))
            }
        } catch (e: Exception) {
            Log.e(REGISTRATION_STATE, e.localizedMessage ?: "Resend code failed due to", e)
            emit(DataState.Error(ErrorHandler.handleException(e)))
        }
    }
}