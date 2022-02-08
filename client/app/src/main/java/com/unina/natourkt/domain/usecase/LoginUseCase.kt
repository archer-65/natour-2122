package com.unina.natourkt.domain.usecase

import com.amplifyframework.auth.AuthException
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.remote.repository.data.AmplifyAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val amplify: AmplifyAuth
) {

    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading())
            val isSignInComplete = amplify.login(username, password)

            if (isSignInComplete) {
                emit(DataState.Success(isSignInComplete))
            } else {
                emit(DataState.Error("Credentials wrong"))
            }
        } catch (e: AuthException) {
            emit(DataState.Error(e.localizedMessage ?: "Authentication error, retry."))
        }
    }
}