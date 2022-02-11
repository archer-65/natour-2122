package com.unina.natourkt.presentation

import androidx.lifecycle.ViewModel
import com.unina.natourkt.domain.usecase.auth.AuthStateUseCase
import com.unina.natourkt.domain.usecase.datastore.GetUserFromStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * ViewModel used by [MainActivity]
 * very basic, only for auth state
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val authStateUseCase: AuthStateUseCase,
    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
) : ViewModel() {

    /**
     * This ARE NOT UiStates
     */
    val isUserAuthenticated
        get() = runBlocking {
            authStateUseCase()
        }

    val loggedUser
        get() = runBlocking {
            getUserFromStoreUseCase()
        }
}