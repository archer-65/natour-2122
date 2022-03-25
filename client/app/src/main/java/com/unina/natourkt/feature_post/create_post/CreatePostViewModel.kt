package com.unina.natourkt.feature_post.create_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.model.PostCreation
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.safeRemove
import com.unina.natourkt.core.domain.use_case.post.CreatePostUseCase
import com.unina.natourkt.core.domain.use_case.route.GetRouteTitleUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.presentation.model.RouteTitleUi
import com.unina.natourkt.core.presentation.model.mapper.RouteTitleUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val getRouteTitleUseCase: GetRouteTitleUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val analytics: ActionAnalyticsUseCase,
    private val routeTitleUiMapper: RouteTitleUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState = _uiState.asStateFlow()

    private val _upcomingRoutes = MutableStateFlow(emptyList<RouteTitleUi>())
    val upcomingRoutes = _upcomingRoutes.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.EnteredQuery -> updateRoutes(event.query)
            is CreatePostEvent.EnteredRoute -> setRoute(event.title)
            is CreatePostEvent.EnteredDescription -> setDescription(event.description)
            is CreatePostEvent.InsertedPhotos -> setPhotos(event.photos)
            is CreatePostEvent.RemovePhoto -> removePhoto(event.position)
            CreatePostEvent.Upload -> uploadPost()
        }
    }

    private fun setRoute(title: String) {
        _uiState.update {
            val selected =
                upcomingRoutes.value.filter { it.routeTitle == title }.firstOrNull()
            it.copy(route = selected)
        }
    }

    private fun setDescription(description: String) {
        _uiState.update {
            it.copy(postDescription = it.postDescription.copy(text = description))
        }
    }

    private fun setPhotos(photos: List<Uri>) {
        _uiState.update {
            it.copy(postPhotos = photos)
        }
    }

    private fun removePhoto(position: Int) {
        _uiState.update {
            it.copy(postPhotos = it.postPhotos.safeRemove(position))
        }
    }

    private fun updateRoutes(query: String) {
        getRouteTitleUseCase(query).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val routeTitlesUi = result.data?.map { routeTitleUiMapper.mapToUi(it) }

                    _upcomingRoutes.value = routeTitlesUi ?: emptyList()
                }
                is DataState.Error -> {}
                is DataState.Loading -> {}
            }

        }.launchIn(viewModelScope)
    }

    private fun uploadPost() {
        viewModelScope.launch {
            createPostUseCase(mapForCreation()).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        analytics.sendEvent(ActionEvents.CreatePost)

                        _uiState.update {
                            it.copy(isInserted = true, isLoading = false)
                        }
                    }
                    is DataState.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    is DataState.Loading -> _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun mapForCreation(): PostCreation {
        return PostCreation(
            description = uiState.value.postDescription.text,
            photos = uiState.value.postPhotos.map { it.toString() },
            author = getUserDataUseCase(),
            taggedRoute = routeTitleUiMapper.mapToDomain(uiState.value.route!!)
        )
    }
}




