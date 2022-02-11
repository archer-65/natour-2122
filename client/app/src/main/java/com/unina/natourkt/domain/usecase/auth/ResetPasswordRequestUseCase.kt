package com.unina.natourkt.domain.usecase.auth

import android.util.Log
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class ResetPasswordRequestUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(username: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            val isPasswordReset = authRepository.resetPasswordRequest(username)

            if (isPasswordReset) {
                emit(DataState.Success(isPasswordReset))
            } else {
                emit(DataState.Error(DataState.CustomMessages.SomethingWentWrong("Unknown Error resetting password")))
            }
        } catch (e: Exception) {
            Log.e(
                "RESET PASSWORD STATE",
                e.localizedMessage ?: "Resetting password failed due to exception",
                e
            )
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}