package com.unina.natourkt.domain.usecase

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.Constants.AMPLIFY
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.data.repository.AuthRepositoryImpl
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ConfirmationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(
        username: String,
        code: String
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())
            val isSignUpComplete = authRepository.confirmRegistration(username, code)

            if (isSignUpComplete) {
                val user = userRepository.getUserByCognitoId(authRepository.fetchUserSub()).toUser()
                dataStoreRepository.saveUserToDataStore(user);

                emit(DataState.Success(isSignUpComplete))
            } else {
                emit(DataState.Error("Something went wrong, retry."))
            }
        } catch (e: AuthException) {
            Log.e(AMPLIFY, "Error is ", e)
            val message: String = when (e) {
                is AuthException.CodeDeliveryFailureException -> "Error in delivering the confirmation code, require another code."

                is AuthException.CodeMismatchException -> "Confirmation code is not correct, retry."

                is AuthException.CodeExpiredException -> "Confirmation code has expired"

                else -> e.localizedMessage ?: "Unknown error, retry later."
            }

            emit(DataState.Error(message))
        } catch (e: HttpException) {
            emit(DataState.Error("Unexpected error, retry"))
        } catch (e: IOException) {
            emit(DataState.Error("Check your internet connection"))
        }
    }
}