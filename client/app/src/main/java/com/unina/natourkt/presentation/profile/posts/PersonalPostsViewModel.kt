package com.unina.natourkt.presentation.profile.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toGridUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.post.GetPersonalPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [PersonalPostsFragment]
 */
@HiltViewModel
class PersonalPostsViewModel @Inject constructor(
    private val getPersonalPostsUseCase: GetPersonalPostsUseCase,
) : ViewModel() {

    /**
     * [PersonalPostsUiState] with a set of RouteItemUiState
     */
    private val _uiState = MutableStateFlow(PersonalPostsUiState())
    val uiState: StateFlow<PersonalPostsUiState> = _uiState.asStateFlow()

    init {
        getPersonalPosts()
    }

    fun getPersonalPosts() {

        viewModelScope.launch {

            // Get routes and map to ItemUiState
            val posts = getPersonalPostsUseCase.invoke()
                .cachedIn(viewModelScope)
                .first()
                .map { it.toGridUi() }

            _uiState.update {
                it.copy(postItems = posts)
            }
        }
    }
}