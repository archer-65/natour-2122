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

    val postId = savedState.get<Long>("postId")

    private val _uiState = MutableStateFlow(PostDetailsUiState())
    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()

    init {
        getLoggedUser()
        getPostDetails(postId!!)
    }

    private fun getPostDetails(id: Long) {
        getPostDetailsUseCase(id).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val postUi = result.data?.let { postDetailsUiMapper.mapToUi(it) }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            post = postUi?.convertKeys {
                                getUrlFromKeyUseCase(it)
                            })
                    }
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }

                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true, error = null)
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