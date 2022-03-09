package com.unina.natourkt.presentation.profile.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toGridUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.post.GetPersonalPostsUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.CompilationItemUiState
import com.unina.natourkt.presentation.base.ui_state.PostGridItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
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
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    /**
     * [PersonalPostsUiState] with a set of RouteItemUiState
     */
    // private val _uiState = MutableStateFlow(PersonalPostsUiState())
    // val uiState: StateFlow<PersonalPostsUiState> = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostGridItemUiState>>
    val postsFlow: Flow<PagingData<PostGridItemUiState>>
        get() = _postsFlow

    init {
        getPersonalPosts()
    }

    private fun getPersonalPosts() {
        viewModelScope.launch {
            _postsFlow = getPersonalPostsUseCase()
                .map { pagingData ->
                    pagingData.map { post ->
                        post.toGridUi().convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}