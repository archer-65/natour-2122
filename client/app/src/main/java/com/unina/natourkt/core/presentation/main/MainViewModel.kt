package com.unina.natourkt.core.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.use_case.auth.GetAuthStateUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    suspend fun getLoggedUser(): User? = withContext(Dispatchers.IO) {
        val user = getUserDataUseCase()
        Log.i("User", user.toString())
        user
    }
}
