package com.unina.natourkt.presentation.new_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.unina.natourkt.common.safeRemove
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(

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

    fun removePhoto(position: Int) {
        _uiState.update {
            it.copy(postPhotos = it.postPhotos.safeRemove(position))
        }
    }
}
