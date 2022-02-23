package com.unina.natourkt.presentation.post_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.toDetailUi
import com.unina.natourkt.domain.use_case.post.GetPostDetailsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailsUiState())

    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()

    fun getPostDetails(id: Long) {
        viewModelScope.launch {

            getPostDetailsUseCase(id).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        _uiState.value = PostDetailsUiState(post = result.data?.toDetailUi())
                    }
                    is DataState.Error -> {
                        _uiState.value = PostDetailsUiState(error = result.error)
                    }

                    is DataState.Loading -> {
                        _uiState.value = PostDetailsUiState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}