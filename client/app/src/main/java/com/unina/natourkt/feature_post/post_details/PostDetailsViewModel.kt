package com.unina.natourkt.feature_post.post_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.post.GetPostDetailsUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.mapper.PostDetailsUiMapper
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val postDetailsUiMapper: PostDetailsUiMapper,
    private val userUiMapper: UserUiMapper,
    savedState: SavedStateHandle
) : ViewModel() {

    private val postId = savedState.get<Long>("postId")

    private val _uiState = MutableStateFlow(PostDetailsUiState())
    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLoggedUser()
        getPostDetails()
    }

    private fun getPostDetails() {
        getPostDetailsUseCase(postId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val postUi = result.data?.let { postDetailsUiMapper.mapToUi(it) }
                    val postDetails = postUi?.convertKeys { getUrlFromKeyUseCase(it) }

                    _uiState.update {
                        it.copy(isLoading = false, post = postDetails)
                    }
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }

                    val errorText = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEvent.ShowSnackbar(errorText))
                }
                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getLoggedUser() {
        viewModelScope.launch {
            _uiState.update {
                val userUi = userUiMapper.mapToUi(getUserDataUseCase()!!)
                it.copy(loggedUser = userUi)
            }
        }
    }
}