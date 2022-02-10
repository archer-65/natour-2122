package com.unina.natourkt.domain.usecase

import android.util.Log
import androidx.datastore.core.CorruptionException
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
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
        } catch (e: CorruptionException) {
            emit(DataState.Error("Try to reinstall app, data may be corrupted"))
        } catch (e: HttpException) {
            emit(DataState.Error("Unexpected error, retry"))
        } catch (e: IOException) {
            emit(DataState.Error("Check your internet connection"))
        }
    }
}