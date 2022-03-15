package com.unina.natourkt.feature_post.create_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.safeRemove
import com.unina.natourkt.core.domain.use_case.post.CreatePostUseCase
import com.unina.natourkt.core.domain.use_case.route.GetRouteTitleUseCase
import com.unina.natourkt.core.presentation.model.mapper.RouteTitleUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val getRouteTitleUseCase: GetRouteTitleUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val routeTitleUiMapper: RouteTitleUiMapper
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
                    val routeTitleUi = result.data?.map { routeTitleUiMapper.mapToUi(it) }
                    val updated = RouteResultsUiState(routeTitleUi ?: emptyList())

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




