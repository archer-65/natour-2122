package com.unina.natourkt.feature_post.report_post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.post.ReportPostUseCase
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ReportPostViewModel @Inject constructor(
    private val reportPostUseCase: ReportPostUseCase,
    private val analytics: ActionAnalyticsUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    private val postId = savedState.get<Long>("postToReportId")

    private val _uiState = MutableStateFlow(ReportPostUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ReportPostEvent) {
        when (event) {
            ReportPostEvent.SendReport -> sendReport()
        }
    }

    private fun sendReport() {
        reportPostUseCase(postId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val text = UiText.StringResource(R.string.post_reported_success)
                    _eventFlow.emit(UiEffect.ShowToast(text))

                    analytics.sendEvent(ActionEvents.ReportPost)

                    _uiState.update { it.copy(isLoading = false, isReported = true) }
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