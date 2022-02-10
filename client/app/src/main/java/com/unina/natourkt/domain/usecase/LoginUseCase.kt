package com.unina.natourkt.domain.usecase

import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.repository.AuthRepositoryImpl
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
) {

    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            val isSignInComplete = authRepository.login(username, password)

            if (isSignInComplete) {
                emit(DataState.Success(isSignInComplete))
            } else {
                emit(DataState.Error("Credentials wrong"))
            }
        } catch (e: AuthException) {
            val message: String = when (e) {
                is AuthException.UserNotFoundException -> "User not found, enter the correct username."

                is AuthException.UserNotConfirmedException -> "User not confirmed, please contact assistance."

                is AuthException.InvalidPasswordException -> "Given password invalid, enter the correct password"

                else -> e.localizedMessage ?: "Unknown error, retry later."
            }

            emit(DataState.Error(message))
        }
    }
}