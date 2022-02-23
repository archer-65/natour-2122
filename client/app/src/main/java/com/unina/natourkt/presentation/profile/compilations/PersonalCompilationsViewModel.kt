package com.unina.natourkt.presentation.profile.compilations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toGridUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.compilation.GetPersonalCompilationsUseCase
import com.unina.natourkt.presentation.profile.posts.PersonalPostsFragment
import com.unina.natourkt.presentation.profile.posts.PersonalPostsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [PersonalCompilationsFragment]
 */
@HiltViewModel
class PersonalCompilationsViewModel @Inject constructor(
    private val getPersonalCompilationsUseCase: GetPersonalCompilationsUseCase,
) : ViewModel() {

    /**
     * [PersonalCompilationsUiState] with a set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(PersonalCompilationsUiState())
    val uiState: StateFlow<PersonalCompilationsUiState> = _uiState.asStateFlow()

    init {
        getPersonalPosts()
    }

    fun getPersonalPosts() {

        viewModelScope.launch {

            // Get routes and map to ItemUiState
            val compilations = getPersonalCompilationsUseCase()
                .cachedIn(viewModelScope)
                .first()
                .map { it.toUi() }

            _uiState.update {
                it.copy(compilationItems = compilations)
            }
        }
    }
}