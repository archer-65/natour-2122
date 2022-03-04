package com.unina.natourkt.presentation.main

import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.model.convertKeys
import com.unina.natourkt.domain.use_case.auth.GetAuthStateUseCase
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.log

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

    val isUserAuthenticated: Boolean
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

    suspend fun getLoggedUser(): User? = withContext(Dispatchers.IO) {
        return@withContext getUserFromStoreUseCase().also {
            it?.convertKeys {
                getUrlFromKeyUseCase(it)
            }
        }
    }
}
