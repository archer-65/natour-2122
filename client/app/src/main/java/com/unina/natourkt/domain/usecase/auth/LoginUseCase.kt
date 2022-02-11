package com.unina.natourkt.domain.usecase.auth

import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import com.unina.natourkt.domain.usecase.datastore.SaveUserToStoreUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of
 * - [AuthRepository] to login the user
 * - [DataStoreRepository] to persist the user on DataStore Preferences
 * - [UserRepository] to retrieve the user through REST Service
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val saveUserToStoreUseCase: SaveUserToStoreUseCase,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            val isSignInComplete = authRepository.login(username, password)

            if (isSignInComplete) {
                val cognitoId = authRepository.fetchUserSub()
                val user = userRepository.getUserByCognitoId(cognitoId).toUser()

                saveUserToStoreUseCase(user)

                emit(DataState.Success(isSignInComplete))
            } else {
                emit(DataState.Error(DataState.CustomMessages.SomethingWentWrong("Unknown Error")))
            }
        } catch (e: Exception) {
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}