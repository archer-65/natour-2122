package com.unina.natourkt.presentation.new_post

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.safeRemove
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.route.GetRouteTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val getRouteTitleUseCase: GetRouteTitleUseCase

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

fun updateRoutes(title: String) {
    getRouteTitleUseCase(title).onEach  { result ->
        when(result){
            is DataState.Success -> {
                _upcomingRoutes.value = RouteResultsUiState(result.data?.map{
                    it.toUi()
                }?: emptyList())
            }
        }

    }
}

}




