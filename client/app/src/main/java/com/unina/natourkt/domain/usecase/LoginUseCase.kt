package com.unina.natourkt.domain.usecase

import android.content.Context
import android.util.Log
import androidx.datastore.core.CorruptionException
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * This UseCase make use of
 * - [AuthRepository] to login the user
 * - [DataStoreRepository] to persist the user on DataStore Preferences
 * - [UserRepository] to retrieve the user through REST Service
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
) {

    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())

            val isSignInComplete = authRepository.login(username, password)

            if (isSignInComplete) {
                val user = userRepository.getUserByCognitoId(authRepository.fetchUserSub()).toUser()
                dataStoreRepository.saveUserToDataStore(user);

                emit(DataState.Success(isSignInComplete))
            } else {
                emit(DataState.Error(context.getString(R.string.auth_failed_generic)))
            }
        } catch (e: AuthException) {
            val message: String = when (e) {
                is AuthException.UserNotFoundException -> context.getString(R.string.user_not_found)

                is AuthException.UserNotConfirmedException -> context.getString(R.string.user_not_confirmed)

                is AuthException.InvalidPasswordException -> context.getString(R.string.password_not_valid_for_user)

                else -> context.getString(R.string.auth_failed_exception)
            }
            
            emit(DataState.Error(message))
        } catch (e: CorruptionException) {
            emit(DataState.Error(context.getString(R.string.data_corrupted)))
        } catch (e: HttpException) {
            emit(DataState.Error(context.getString(R.string.retrofit_http_error)))
        } catch (e: IOException) {
            emit(DataState.Error(context.getString(R.string.internet_error)))
        }
    }
}