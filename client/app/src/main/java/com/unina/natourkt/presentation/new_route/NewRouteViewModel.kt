package com.unina.natourkt.presentation.new_route

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.safeRemove
import com.unina.natourkt.domain.use_case.maps.GetDirectionsUseCase
import com.unina.natourkt.domain.use_case.storage.UploadFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRouteViewModel @Inject constructor(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val uploadFilesUseCase: UploadFilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRouteUiState())
    val uiState: StateFlow<NewRouteUiState> = _uiState.asStateFlow()

    fun setInfo(routeInfo: NewRouteInfo) {
        _uiState.update { currentState ->
            currentState.copy(routeInfo = routeInfo)
        }
    }

    fun addStop(latitude: Double, longitude: Double) {
        _uiState.update { currentState ->
            val newStops = currentState.routeStops + NewRouteStop(
                stopNumber = currentState.routeStops.size + 1,
                latitude,
                longitude
            )
            currentState.copy(routeStops = newStops)
        }
    }

    fun getDirections() {
        val stops = uiState.value.routeStops.map { it.toRouteStop() }
        getDirectionsUseCase(stops).onEach { result ->
            when (result) {

                is DataState.Success -> {
                    val polylines = PolylineOptions()
                    result.data?.let { polylines.addAll(it.points) }
                    _uiState.update { it.copy(polylineOptions = polylines) }
                }
                is DataState.Error -> {
                }
                is DataState.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun setPhotos(photos: List<Uri>) {
        _uiState.update {
            it.copy(routePhotos = photos)
        }
    }

    fun removePhoto(position: Int) {
        _uiState.update {
            it.copy(routePhotos = it.routePhotos.safeRemove(position))
        }
    }

    fun uploadRoute() {
        viewModelScope.launch {
            val url = uploadFilesUseCase(uiState.value.routePhotos.first())
            Log.i("Url s3", url.toString())
        }
    }
}

