package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.common.Constants
import com.unina.natourkt.common.Constants.LOGIN_STATE
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

    /**
     * Use this one for classic login
     */
    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            Log.i(LOGIN_STATE, "Processing login request...")

            val isSignInComplete = authRepository.login(username, password)
            if (isSignInComplete) {
                // If user's login is successful, save the user locally in DataStore Preferences
                Log.i(LOGIN_STATE, "Login ok!")
                localAuthStateManager()

                Log.i(LOGIN_STATE, "Login successful!")
                emit(DataState.Success(isSignInComplete))
            } else {
                Log.e(LOGIN_STATE, "Whoops, something went wrong with authentication")
                emit(DataState.Error(DataState.CustomMessages.AuthGeneric))
            }
        } catch (e: Exception) {
            Log.e(LOGIN_STATE, e.localizedMessage ?: "Login failed", e)
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }

    /**
     * Use this one for social login
     */
    operator fun invoke(provider: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            Log.i(LOGIN_STATE, "Processing login request...")

            val isSignInComplete = authRepository.login(provider)
            if (isSignInComplete) {
                // If user's login is successful, save the user locally in DataStore Preferences
                Log.i(LOGIN_STATE, "Login with social provider ok!")
                localAuthStateManager()

                Log.i(LOGIN_STATE, "Login successful!")
                emit(DataState.Success(isSignInComplete))
            } else {
                Log.e(LOGIN_STATE, "Whoops, something went wrong with authentication")
                emit(DataState.Error(DataState.CustomMessages.AuthGeneric))
            }
        } catch (e: Exception) {
            Log.e(LOGIN_STATE, e.localizedMessage ?: "Social login failed", e)
            emit(DataState.Error(errorHandler.handleException<Throwable>(e)))
        }
    }

    /**
     * Save to DataStore the logged user
     */
    private suspend fun localAuthStateManager() {
        Log.i(LOGIN_STATE, "Retreving User Sub...")
        val userUUID = authRepository.fetchUserSub()

        Log.i(LOGIN_STATE, "Getting user data from API...")
        val user = userRepository.getUserByCognitoId(userUUID).toUser()

        Log.i(LOGIN_STATE, "Saving user data locally...")
        saveUserToStoreUseCase(user)
    }
}