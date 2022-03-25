package com.unina.natourkt.core.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.Constants.REGISTRATION_STATE
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to confirm a registered user through code received by email
 * @see [AuthRepository]
 */
class RegistrationConfirmationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(username: String, code: String): Flow<DataState<Boolean>> = flow {
        Log.i(REGISTRATION_STATE, "Processing sign up confirmation...")
        emit(DataState.Loading())

        val isSignUpComplete = authRepository.confirmRegistration(username, code)
        emit(isSignUpComplete)
    }
}