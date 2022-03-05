package com.unina.natourkt.domain.use_case.auth

import android.util.Log
import android.webkit.URLUtil
import com.unina.natourkt.common.Constants.LOGIN_STATE
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import com.unina.natourkt.domain.use_case.datastore.SaveUserToStoreUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

/**
 * This UseCase makes use of [AuthRepository] to register an user
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    /**
     * Use this one for classic login
     */
    operator fun invoke(username: String, password: String): Flow<DataState<Boolean>> = flow {
        Log.i(LOGIN_STATE, "Processing login request...")
        emit(DataState.Loading())

        val loginResult = authRepository.login(username, password)
        emit(loginResult)
    }

    /**
     * Use this one for social login
     */
    operator fun invoke(provider: String): Flow<DataState<Boolean>> = flow {
        Log.i(LOGIN_STATE, "Processing login request...")
        emit(DataState.Loading())

        val loginResult = authRepository.login(provider)
        emit(loginResult)
    }
}