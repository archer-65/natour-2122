package com.unina.natourkt.presentation.new_post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.safeRemove
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.post.CreatePostUseCase
import com.unina.natourkt.domain.use_case.route.GetRouteTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val getRouteTitleUseCase: GetRouteTitleUseCase,
    private val createPostUseCase: CreatePostUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewPostUiState())
    val uiState: StateFlow<NewPostUiState> = _uiState.asStateFlow()

    private val _upcomingRoutes = MutableStateFlow(RouteResultsUiState())
    val upcomingRoutes: StateFlow<RouteResultsUiState> = _upcomingRoutes.asStateFlow()

    fun setPhotos(photos: List<Uri>) {
        _uiState.update {
            it.copy(postPhotos = photos)
        }
    }

    fun setRoute(title: String) {
        _uiState.update {
            val selected =
                upcomingRoutes.value.routes.filter { it.routeTitle == title }.firstOrNull()
            it.copy(route = selected)
        }
    }

    fun removePhoto(position: Int) {
        _uiState.update {
            it.copy(postPhotos = it.postPhotos.safeRemove(position))
        }
    }

    fun updateRoutes(title: String) {
        getRouteTitleUseCase(title).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val updated = RouteResultsUiState(result.data?.map {
                        it.toUi()
                    } ?: emptyList())

                    Log.i("CIAO", updated.toString())

                    _upcomingRoutes.value = updated
                }
                is DataState.Error -> {}
                is DataState.Loading -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun uploadPost() {
        createPostUseCase(uiState.value.toPostCreation()).onEach { result ->
            when (result) {
                is DataState.Success -> _uiState.update {
                    it.copy(isInserted = true, isLoading = false, errorMessage = null)
                }
                is DataState.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error)
                }
                is DataState.Loading -> _uiState.update {
                    it.copy(isLoading = true, errorMessage = null)
                }
            }
        }.launchIn(viewModelScope)
    }
}




