package com.unina.natourkt.core.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.Constants.PASSWORD_RESET
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase provides password's reset in case of forgot password
 * @see [AuthRepository]
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