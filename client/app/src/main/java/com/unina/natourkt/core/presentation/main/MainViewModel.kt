package com.unina.natourkt.core.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.use_case.analytics.MainAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.auth.GetAuthStateUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
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
    private val analyticsUseCase: MainAnalyticsUseCase,
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

    fun destinationChanged(destinationId: Int) {
        analyticsUseCase.destinationChanged(destinationId)
    }

    fun sessionStarted() {
        analyticsUseCase.sessionStarted()
    }
}
