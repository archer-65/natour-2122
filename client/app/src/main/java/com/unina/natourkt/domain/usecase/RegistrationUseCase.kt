package com.unina.natourkt.domain.usecase

import android.content.Context
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.repository.AuthRepositoryImpl
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.InvalidParameterException
import javax.inject.Inject

/**
 * This UseCase make use of [AuthRepository] to register a user
 */
class RegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
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
                emit(DataState.Error(context.getString(R.string.auth_failed_generic)))
            }
        } catch (e: AuthException) {
            val message: String = when (e) {
                is AuthException.UsernameExistsException -> context.getString(R.string.username_exists)

                is AuthException.AliasExistsException -> context.getString(R.string.credentials_already_taken)

                is AuthException.InvalidParameterException -> context.getString(R.string.incorrect_parameters)

                else -> context.getString(R.string.auth_failed_exception)
            }

            emit(DataState.Error(message))
        }
    }
}