package com.unina.natourkt.feature_route.report_route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.model.ReportCreation
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.report.CreateRouteReportUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportRouteViewModel @Inject constructor(
    private val createRouteReportUseCase: CreateRouteReportUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val analytics: ActionAnalyticsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routeId = savedStateHandle.get<Long>("reportedRouteId")

    private val _uiState = MutableStateFlow(ReportRouteUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ReportRouteEvent) {
        when (event) {
            is ReportRouteEvent.EnteredTitle -> setTitle(event.title)
            is ReportRouteEvent.EnteredDescription -> setDescription(event.description)
            is ReportRouteEvent.Upload -> uploadReport()
        }
    }

    private fun setTitle(title: String) {
        _uiState.update {
            it.copy(reportTitle = it.reportTitle.copy(text = title))
        }
    }

    private fun setDescription(description: String) {
        _uiState.update {
            it.copy(reportDescription = it.reportDescription.copy(text = description))
        }
    }

    private fun uploadReport() {
        viewModelScope.launch {
            createRouteReportUseCase(mapForCreation()).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.update {
                            it.copy(isInserted = true, isLoading = false)
                        }

                        analytics.sendEvent(ActionEvents.ReportRoute)

                        val text = UiText.StringResource(R.string.report_inserted)
                        _eventFlow.emit(UiEffect.ShowToast(text))
                    }
                    is DataState.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    is DataState.Loading -> _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun mapForCreation(): ReportCreation {
        return ReportCreation(
            title = uiState.value.reportTitle.text,
            description = uiState.value.reportDescription.text,
            author = getUserDataUseCase(),
            reportedRouteId = routeId!!
        )
    }
}