package com.unina.natourkt.domain.usecase

import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.remote.repository.data.AmplifyAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val amplify: AmplifyAuth
) {

    operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())
            val isSignUpComplete = amplify.register(username, email, password)

            if (isSignUpComplete) {
                emit(DataState.Success(isSignUpComplete))
            } else {
                emit(DataState.Error("Something went wrong, retry."))
            }
        } catch (e: AuthException) {
            emit(DataState.Error(e.localizedMessage ?: "Sign up failed, check up credentials"))
        }
    }
}