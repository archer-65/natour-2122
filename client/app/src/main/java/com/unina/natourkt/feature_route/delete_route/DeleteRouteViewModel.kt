package com.unina.natourkt.feature_post.delete_post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.route.DeleteRouteUseCase
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.feature_route.delete_route.DeleteRouteEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DeleteRouteViewModel @Inject constructor(
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val analytics: ActionAnalyticsUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    private val postId = savedState.get<Long>("routeToDeleteId")

    private val _uiState = MutableStateFlow(DeleteRouteUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: DeleteRouteEvent) {
        when (event) {
            DeleteRouteEvent.OnDelete -> deleteRoute()
        }
    }

    private fun deleteRoute() {
        deleteRouteUseCase(postId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val text = UiText.StringResource(R.string.deleted_route_success)
                    _eventFlow.emit(UiEffect.ShowToast(text))

                    analytics.sendEvent(ActionEvents.DeleteRoute)

                    _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                }
                is DataState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is DataState.Error -> {
                    _uiState.update { it.copy(isLoading = false) }

                    val text = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowToast(text))
                }
            }
        }.launchIn(viewModelScope)
    }
}