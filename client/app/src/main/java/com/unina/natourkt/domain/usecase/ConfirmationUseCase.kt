package com.unina.natourkt.domain.usecase

import android.content.Context
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of
 * - [AuthRepository] to confirm the user registration through code
 * - [DataStoreRepository] to persist the user on DataStore Preferences
 * - [UserRepository] to retrieve the user through REST Service
 */
class ConfirmationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(
        username: String,
        code: String
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())
            val isSignUpComplete = authRepository.confirmRegistration(username, code)

            if (isSignUpComplete) {
                val cognitoId = authRepository.fetchUserSub()
                val user = userRepository.getUserByCognitoId(cognitoId).toUser()
                dataStoreRepository.saveUserToDataStore(user);

                emit(DataState.Success(isSignUpComplete))
            } else {
                emit(DataState.Error(DataState.CustomMessages.SomethingWentWrong("Unknown Error")))
            }
        } catch (e: Exception) {
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}