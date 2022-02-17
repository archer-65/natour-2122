package com.unina.natourkt.presentation.home

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.post.toUi
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

    fun getPosts(pageNo: Int = 0) {

        viewModelScope.launch {
            getPostsUseCase(pageNo).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        val currentPosts = _uiState.value.postItems
                        val upcomingPosts = result.data?.map { post -> post.toUi() } ?: emptyList()
                        currentPosts.addAll(upcomingPosts)
                        _uiState.update {
                            it.copy(postItems = currentPosts, isLoading = false, errorMessage = null)
                        }
                    }
                    is DataState.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = result.error, isLoading = false)
                        }
                    }
                    is DataState.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}