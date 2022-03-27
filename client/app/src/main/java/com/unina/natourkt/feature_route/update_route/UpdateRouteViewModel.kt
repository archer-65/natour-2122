package com.unina.natourkt.feature_route.update_route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.route.UpdateRouteUseCase
import com.unina.natourkt.core.presentation.model.RouteDetailsUi
import com.unina.natourkt.core.presentation.model.mapper.RouteDetailsUiMapper
import com.unina.natourkt.core.presentation.util.TextFieldState
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UpdateRouteViewModel @Inject constructor(
    private val updateRouteUseCase: UpdateRouteUseCase,
    private val routeDetailsUiMapper: RouteDetailsUiMapper,
    private val analyticsUseCase: ActionAnalyticsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.get<RouteDetailsUi>("routeToUpdate")!!

    private val _uiState =
        MutableStateFlow(UpdateRouteUiState(newDescription = TextFieldState(text = route.description)))
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: UpdateRouteEvent) {
        when (event) {
            is UpdateRouteEvent.EnteredDescription -> setDescription(event.description)
            is UpdateRouteEvent.Upload -> updateRoute()
        }
    }

    private fun setDescription(description: String) {
        _uiState.update {
            it.copy(newDescription = it.newDescription.copy(text = description))
        }
    }

    private fun updateRoute() {
        val routeUiToInsert = route.copy(description = uiState.value.newDescription.text)
        val routeToInsert = routeDetailsUiMapper.mapToDomain(routeUiToInsert)

        updateRouteUseCase(routeToInsert).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    _uiState.update {
                        it.copy(isUpdated = true, isLoading = false)
                    }

                    analyticsUseCase.sendEvent(ActionEvents.UpdateRoute)

                    val text = UiText.StringResource(R.string.updated_route)
                    _eventFlow.emit(UiEffect.ShowToast(text))
                }
                is DataState.Loading -> _uiState.update {
                    it.copy(isLoading = true)
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }

                    val errorText = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowToast(errorText))
                }
            }
        }.launchIn(viewModelScope)
    }
}