package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.common.Constants.PASSWORD_RESET
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of [AuthRepository] to reset password (request step)
 */
class ResetPasswordRequestUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    /**
     * Reset password request
     */
    operator fun invoke(username: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            Log.i(PASSWORD_RESET, "Processing password reset request...")

            val isPasswordReset = authRepository.resetPasswordRequest(username)
            if (isPasswordReset) {
                // If reset is successful emit true
                Log.i(PASSWORD_RESET, "Password reset request sent!")
                emit(DataState.Success(isPasswordReset))
            } else {
                Log.e(PASSWORD_RESET, "Whoops, something went wrong with code request, retry!")
                emit(DataState.Error(DataState.CustomMessage.CodeDelivery))
            }
        } catch (e: Exception) {
            Log.e(PASSWORD_RESET, e.localizedMessage ?: "Password reset request failed", e)
            emit(DataState.Error(errorHandler.handleException(e)))
        }
    }
}