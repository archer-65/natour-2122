package com.unina.natourkt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.post.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getPosts()
    }

    fun getPosts() {

        viewModelScope.launch {
            val posts = getPostsUseCase()
                .cachedIn(viewModelScope)
                .first()
                .map { it.toUi() }

            _uiState.update {
                it.copy(postItems = posts)
            }
        }
    }
}