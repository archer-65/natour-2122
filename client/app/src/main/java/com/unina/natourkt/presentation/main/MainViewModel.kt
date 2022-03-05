package com.unina.natourkt.presentation.main

import androidx.lifecycle.ViewModel
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.auth.GetAuthStateUseCase
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.UserUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
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
    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
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
        getAuthStateUseCase()
    }

    suspend fun getLoggedUser(): UserUiState? = withContext(Dispatchers.IO) {
        return@withContext getUserFromStoreUseCase()?.toUi().also {
            it?.convertKeys {
                getUrlFromKeyUseCase(it)
            }
        }
    }
}
