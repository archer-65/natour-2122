package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import com.unina.natourkt.domain.use_case.datastore.SaveUserToStoreUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase make use of
 * - [AuthRepository] to login the user through socials
 * - [DataStoreRepository] to persist the user on DataStore Preferences
 * - [UserRepository] to retrieve the user through REST Service
 */
class LoginSocialUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val saveUserToStoreUseCase: SaveUserToStoreUseCase,
    private val errorHandler: ErrorHandler,
) {

    operator fun invoke(provider: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            val isSignInComplete = authRepository.login(provider)

            if (isSignInComplete) {
                // If user's login is successful, save the user locally in DataStore Preferences
                val cognitoId = authRepository.fetchUserSub()
                val user = userRepository.getUserByCognitoId(cognitoId).toUser()

                saveUserToStoreUseCase(user)

                emit(DataState.Success(isSignInComplete))
            } else {
                emit(DataState.Error(DataState.CustomMessages.SomethingWentWrong("Unknown Error logging with social")))
            }
        } catch (e: Exception) {
            Log.e(
                "LOGIN SOCIAL STATE",
                e.localizedMessage ?: "Social login failed due to exception",
                e
            )
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }
}