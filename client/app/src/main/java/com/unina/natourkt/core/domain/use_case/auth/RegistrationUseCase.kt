package com.unina.natourkt.core.domain.use_case.auth

import androidx.core.util.PatternsCompat
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase provides sign up functionality through username, email and password
 */
class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())

        val isValid = formValidator(username, email, password)
        if (isValid is DataState.Error) {
            return@flow
        }

        val signUpResult = authRepository.register(username, email, password)
        emit(signUpResult)
    }

    fun formValidator(username: String, email: String, password: String): DataState<Unit> {
        if (username.contains(" ") || username.isBlank()) {
            return DataState.Error(DataState.Cause.InvalidUsername)
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return DataState.Error(DataState.Cause.InvalidEmail)
        }
        if (password.length < 8) {
            return DataState.Error(DataState.Cause.InvalidPassword)
        }
        return DataState.Success(Unit)
    }
}