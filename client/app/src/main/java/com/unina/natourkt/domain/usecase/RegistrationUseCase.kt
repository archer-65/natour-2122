package com.unina.natourkt.domain.usecase

import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.repository.AuthRepositoryImpl
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.InvalidParameterException
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())
            val isSignUpComplete = authRepository.register(username, email, password)

            if (isSignUpComplete) {
                emit(DataState.Success(isSignUpComplete))
            } else {
                emit(DataState.Error("Something went wrong, retry."))
            }
        } catch (e: AuthException) {
            val message: String = when (e) {
                is AuthException.UsernameExistsException -> "Username already exists."

                is AuthException.AliasExistsException -> "Credentials already in use."

                is AuthException.InvalidParameterException -> "One or more parameters incorrect, enter correct parameters"

                else -> "Unknown error, retry later."
            }

            emit(DataState.Error(message))
        }
    }
}