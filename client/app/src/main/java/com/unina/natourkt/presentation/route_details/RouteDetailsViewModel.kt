package com.unina.natourkt.presentation.route_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.route.toDetailUi
import com.unina.natourkt.domain.model.toDetailUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.route.GetRouteDetailsUseCase
import com.unina.natourkt.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.post_details.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val getRouteDetailsUseCase: GetRouteDetailsUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    val routeId = savedState.get<Long>("routeId")
    val authorId = savedState.get<Long>("authorId")

    private val _uiState = MutableStateFlow(RouteDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLoggedUser()
        getRouteDetails(routeId!!)
    }

    private fun getRouteDetails(id: Long) {
        getRouteDetailsUseCase(id).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            route = result.data?.toDetailUi()?.convertKeys {
                                getUrlFromKeyUseCase(it)
                            })
                    }
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }

                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getLoggedUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loggedUser = getUserDataUseCase()!!.toUi())
            }
        }
    }
}