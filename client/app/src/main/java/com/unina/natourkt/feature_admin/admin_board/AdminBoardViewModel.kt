package com.unina.natourkt.feature_admin.admin_board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.domain.use_case.report.GetReportsUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.ReportItemUi
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.ReportItemUiMapper
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.feature_route.routes.RoutesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminBoardViewModel @Inject constructor(
    private val getReportsUseCase: GetReportsUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val userUiMapper: UserUiMapper,
    private val reportItemUiMapper: ReportItemUiMapper,
) : ViewModel() {

    /**
     * [RoutesUiState] with set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(AdminBoardUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var _reportsFlow: Flow<PagingData<ReportItemUi>>
    val reportsFlow: Flow<PagingData<ReportItemUi>>
        get() = _reportsFlow

    init {
        getReports()
        getUser()
    }

    private fun getReports() {
        viewModelScope.launch {
            _reportsFlow = getReportsUseCase()
                .map { pagingData ->
                    pagingData.map { report ->
                        val reportUi = reportItemUiMapper.mapToUi(report)
                        reportUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _uiState.update {
                val user = getUserDataUseCase()?.let { userUiMapper.mapToUi(it) }
                it.copy(loggedUser = user)
            }
        }
    }
}