package com.unina.natourkt.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.domain.usecase.AuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authStateUseCase: AuthStateUseCase,
) : ViewModel() {

    val isUserAuthenticated
        get() = runBlocking {
            authStateUseCase()
        }
}