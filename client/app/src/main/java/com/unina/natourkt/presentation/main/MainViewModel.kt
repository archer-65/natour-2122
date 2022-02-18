package com.unina.natourkt.presentation.main

import androidx.lifecycle.ViewModel
import com.unina.natourkt.domain.use_case.auth.GetAuthStateUseCase
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * ViewModel used by [MainActivity]
 * very basic, only for auth state
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
) : ViewModel() {

    /**
     * This ARE NOT UiStates
     */
    val isUserAuthenticated
        get() = runBlocking {
            getAuthStateUseCase()
        }

    val loggedUser
        get() = runBlocking {
            getUserFromStoreUseCase()
        }
}