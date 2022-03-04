package com.unina.natourkt.presentation.profile.compilations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.compilation.GetPersonalCompilationsUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [PersonalCompilationsFragment]
 */
@HiltViewModel
class PersonalCompilationsViewModel @Inject constructor(
    private val getPersonalCompilationsUseCase: GetPersonalCompilationsUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    /**
     * [PersonalCompilationsUiState] with a set of RouteItemUiState
     */
    // private val _uiState = MutableStateFlow(PersonalCompilationsUiState())
    // val uiState: StateFlow<PersonalCompilationsUiState> = _uiState.asStateFlow()

    private lateinit var _compilationsFlow: Flow<PagingData<CompilationItemUiState>>
    val compilationsFlow: Flow<PagingData<CompilationItemUiState>>
        get() = _compilationsFlow

    init {
        getPersonalCompilations()
    }

    private fun getPersonalCompilations() {
        viewModelScope.launch {
            _compilationsFlow = getPersonalCompilationsUseCase()
                .map { pagingData ->
                    pagingData.map { compilation ->
                        compilation.toUi().convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}