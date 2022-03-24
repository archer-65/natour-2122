package com.unina.natourkt.feature_post.delete_post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.R
import com.unina.natourkt.core.domain.use_case.post.DeletePostUseCase
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DeletePostViewModel @Inject constructor(
    private val deletePostUseCase: DeletePostUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    private val postId = savedState.get<Long>("postToDeleteId")

    private val _uiState = MutableStateFlow(DeleteRouteUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: DeletePostEvent) {
        when (event) {
            DeletePostEvent.OnDelete -> deletePost()
        }
    }

    private fun deletePost() {
        deletePostUseCase(postId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val text = UiText.StringResource(R.string.deleted_post_success)
                    _eventFlow.emit(UiEffect.ShowToast(text))

                    _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                }
                is DataState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is DataState.Error -> {
                    _uiState.update { it.copy(isLoading = false) }

                    val text = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowToast(text))
                }
            }
        }.launchIn(viewModelScope)
    }
}