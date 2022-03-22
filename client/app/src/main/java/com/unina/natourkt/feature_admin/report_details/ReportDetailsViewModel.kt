package com.unina.natourkt.feature_admin.report_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.domain.use_case.report.DeleteReportUseCase
import com.unina.natourkt.core.presentation.model.ReportItemUi
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ReportDetailsViewModel @Inject constructor(
    private val deleteReportUseCase: DeleteReportUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    val reportInfo = savedState.get<ReportItemUi>("reportItem")!!

    private val _uiState = MutableStateFlow(ReportDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun deleteReport() {
        deleteReportUseCase(reportInfo.id).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val text = UiText.StringResource(R.string.report_deleted_success)
                    _eventFlow.emit(UiEvent.ShowToast(text))

                    _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                }
                is DataState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is DataState.Error -> {
                    _uiState.update { it.copy(isLoading = false) }

                    val text = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEvent.ShowToast(text))
                }
            }
        }.launchIn(viewModelScope)
    }
}