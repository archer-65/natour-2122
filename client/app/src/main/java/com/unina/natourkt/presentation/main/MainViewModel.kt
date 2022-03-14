package com.unina.natourkt.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.auth.GetAuthStateUseCase
import com.unina.natourkt.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.model.UserUiState
import com.unina.natourkt.presentation.base.model.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel used by [MainActivity]
 * very basic, only for auth state
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    val isUserAuthenticated
        get() = runBlocking {
            getAuthState()
        }

    val loggedUser
        get() = runBlocking {
            getLoggedUser()
        }

    suspend fun getAuthState(): Boolean = withContext(Dispatchers.IO) {
        val auth = getAuthStateUseCase()
        Log.i("Auth", auth.toString())
        auth
    }

    suspend fun getLoggedUser(): UserUiState? = withContext(Dispatchers.IO) {
        val user = getUserDataUseCase()?.toUi().also {
            it?.convertKeys {
                getUrlFromKeyUseCase(it)
            }
        }

        Log.i("USER", user.toString())
        return@withContext user
    }
}
