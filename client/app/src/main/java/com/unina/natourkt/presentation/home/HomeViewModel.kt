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
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    private val _pagingPostsFlow: Flow<PagingData<PostItemUiState>>
    val pagingPostsFlow: Flow<PagingData<PostItemUiState>>
        get() = _pagingPostsFlow

    init {
        _pagingPostsFlow = getPosts()
    }

    private fun getPosts(): Flow<PagingData<PostItemUiState>> {
        return getPostsUseCase()
            .map { pagingData ->
                pagingData.map { post ->
                    post.toUi()
                }
            }
            .cachedIn(viewModelScope)
    }
}