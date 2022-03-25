package com.unina.natourkt.feature_route.rate_route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.rating.RateRouteUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateRouteViewModel @Inject constructor(
    private val rateRouteUseCase: RateRouteUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val analytics: ActionAnalyticsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeId = savedStateHandle.get<Long>("ratedRouteId")

    private val _uiState = MutableStateFlow(RateRouteUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RateRouteEvent) {
        when (event) {
            is RateRouteEvent.EnteredDuration -> setDuration(event.duration)
            is RateRouteEvent.EnteredDifficulty -> setDifficulty(event.difficulty)
            is RateRouteEvent.Upload -> uploadRating()
        }
    }

    private fun setDuration(duration: String) {
        _uiState.update {
            it.copy(duration = it.duration.copy(text = duration))
        }
    }

    private fun setDifficulty(difficulty: Difficulty) {
        _uiState.update {
            it.copy(difficulty = difficulty)
        }
    }

    private fun uploadRating() {
        viewModelScope.launch {
            rateRouteUseCase(mapForCreation()).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.update {
                            it.copy(isInserted = true, isLoading = false)
                        }

                        analytics.sendEvent(ActionEvents.RateRoute)

                        val text = UiText.StringResource(R.string.rating_inserted)
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
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun mapForCreation(): RatingCreation {
        return RatingCreation(
            difficulty = uiState.value.difficulty,
            duration = uiState.value.duration.text.toFloat(),
            author = getUserDataUseCase(),
            ratedRouteId = routeId!!
        )
    }
}