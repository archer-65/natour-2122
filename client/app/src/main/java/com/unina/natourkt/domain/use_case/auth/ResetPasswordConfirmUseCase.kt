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
) {

    operator fun invoke(password: String, code: String): Flow<DataState<Boolean>> = flow {
        Log.i(PASSWORD_RESET, "Processing password reset request...")
        emit(DataState.Loading())

        val isPasswordReset = authRepository.resetPasswordConfirm(password, code)
        emit(isPasswordReset)
    }
}