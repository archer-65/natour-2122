package com.unina.natourkt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.post.GetPostsUseCase
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel used by [HomeFragment]
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {

    /**
     * [HomeUiState], useless, reserved for future usage
     */
    // private val _uiState = MutableStateFlow(HomeUiState())
    // val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostItemUiState>>
    val postsFlow: Flow<PagingData<PostItemUiState>>
        get() = _postsFlow

    init {
        getPosts()
    }

    private fun getPosts() {
        viewModelScope.launch {
            _postsFlow = getPostsUseCase()
                .map { pagingData -> pagingData.map { compilation -> compilation.toUi() } }
                .cachedIn(viewModelScope)
        }
    }
}