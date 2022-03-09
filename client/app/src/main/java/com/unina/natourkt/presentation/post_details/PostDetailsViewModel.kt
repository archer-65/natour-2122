package com.unina.natourkt.presentation.post_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.toDetailUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.domain.use_case.post.GetPostDetailsUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
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
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            post = result.data?.toDetailUi()?.convertKeys {
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
                it.copy(loggedUser = getUserDataUseCase()!!.toUi())
            }
        }
    }
}