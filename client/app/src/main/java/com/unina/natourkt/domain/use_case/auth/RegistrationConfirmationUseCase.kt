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
 * This UseCase make use of
 * - [AuthRepository] to confirm the user registration through code
 */
class RegistrationConfirmationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
) {

    /**
     * Confirm user's account
     */
    operator fun invoke(username: String, code: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            Log.i(REGISTRATION_STATE, "Processing confirmation request...")

            val isSignUpComplete = authRepository.confirmRegistration(username, code)
            if (isSignUpComplete) {
                // If confirmation is successful emit true
                Log.i(REGISTRATION_STATE, "Account confirmation successful!")
                emit(DataState.Success(isSignUpComplete))
            } else {
                Log.e(REGISTRATION_STATE, "Whoops, something went wrong with confirmation, retry!")
                emit(DataState.Error(DataState.CustomMessage.AuthGeneric))
            }
        } catch (e: Exception) {
            Log.e(REGISTRATION_STATE, e.localizedMessage ?: "Confirmation failed", e)
            emit(DataState.Error(ErrorHandler.handleException(e)))
        }
    }
}