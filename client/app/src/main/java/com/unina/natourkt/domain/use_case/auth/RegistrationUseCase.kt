package com.unina.natourkt.domain.use_case.auth

import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of [AuthRepository] to register a user
 */
class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val errorHandler: ErrorHandler,
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
                emit(DataState.Error(DataState.CustomMessages.SomethingWentWrong("Unknown Error")))
            }
        } catch (e: Exception) {
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}