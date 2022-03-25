package com.unina.natourkt.core.domain.use_case.auth

import android.util.Log
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.Constants.LOGIN_STATE
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase provides the login with Social Medias like Google and Facebook
 * @see [AuthRepository]
 */
class LoginSocialUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(provider: String): Flow<DataState<Boolean>> = flow {
        Log.i(LOGIN_STATE, "Processing login attempt via ${provider}...")
        emit(DataState.Loading())

        val loginResult = authRepository.login(provider)
        emit(loginResult)
    }
}