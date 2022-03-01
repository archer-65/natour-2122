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
 * This UseCase make use of [AuthRepository] to reset password (confirmation step)
 */
class ResetPasswordConfirmUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    /**
     * Reset password
     */
    operator fun invoke(password: String, code: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            Log.i(PASSWORD_RESET, "Processing password reset request...")

            authRepository.resetPasswordConfirm(password, code)
            emit(DataState.Success(true))
        } catch (e: Exception) {
            Log.e(PASSWORD_RESET, e.localizedMessage ?: "Resetting password failed", e)
            emit(DataState.Error(errorHandler.handleException(e)))
        }
    }
}