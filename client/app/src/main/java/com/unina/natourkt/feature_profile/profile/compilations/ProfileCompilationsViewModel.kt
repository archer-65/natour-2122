package com.unina.natourkt.feature_profile.profile.compilations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.compilation.GetPersonalCompilationsUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import com.unina.natourkt.core.presentation.model.mapper.CompilationItemUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [ProfileCompilationsFragment]
 */
@HiltViewModel
class ProfileCompilationsViewModel @Inject constructor(
    private val getPersonalCompilationsUseCase: GetPersonalCompilationsUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase,
    private val compilationMapper: CompilationItemUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileCompilationsUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _compilationsFlow: Flow<PagingData<CompilationItemUi>>
    val compilationsFlow: Flow<PagingData<CompilationItemUi>>
        get() = _compilationsFlow

    init {
        getPersonalCompilations()
    }

    fun onEvent(event: ProfileCompilationsEvent) {
        when (event) {
            ProfileCompilationsEvent.ClickCompilation -> analyticsUseCase.sendEvent(ActionEvents.ClickCompilation)
        }
    }

    private fun getPersonalCompilations() {
        viewModelScope.launch {
            _compilationsFlow = getPersonalCompilationsUseCase()
                .map { pagingData ->
                    pagingData.map { compilation ->
                        val compilationUi = compilationMapper.mapToUi(compilation)
                        compilationUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}