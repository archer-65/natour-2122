package com.unina.natourkt.presentation.new_route

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.safeRemove
import com.unina.natourkt.common.toInputStream
import com.unina.natourkt.domain.use_case.maps.GetDirectionsUseCase
import com.unina.natourkt.domain.use_case.route.CreateRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewRouteViewModel @Inject constructor(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val gpxParser: GPXParser,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRouteUiState())
    val uiState: StateFlow<NewRouteUiState> = _uiState.asStateFlow()


    fun setTitle(routeTitle: String) {
        _uiState.update {
            val currentInfo = it.routeInfo.copy(routeTitle = routeTitle)
            it.copy(routeInfo = currentInfo)
        }
    }

    fun setDescription(description: String) {
        _uiState.update {
            val currentInfo = it.routeInfo.copy(routeDescription = description)
            it.copy(routeInfo = currentInfo)
        }
    }

    fun setDuration(duration: String) {
        _uiState.update {
            val currentInfo = it.routeInfo.copy(duration = duration)
            it.copy(routeInfo = currentInfo)
        }
    }

    fun setDisabilityFriendly(checked: Boolean) {
        _uiState.update {
            val currentInfo = it.routeInfo.copy(disabilityFriendly = checked)
            it.copy(routeInfo = currentInfo)
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update {
            val currentInfo = it.routeInfo.copy(difficulty = difficulty)
            it.copy(routeInfo = currentInfo)
        }
    }

    fun addStop(latitude: Double, longitude: Double) {
        _uiState.update { currentState ->
            val newStops = currentState.routeStops + NewRouteStop(
                stopNumber = currentState.routeStops.size + 1,
                latitude,
                longitude
            )
            currentState.copy(routeStops = newStops, isLoadedFromGPX = false)
        }
    }

    fun getDirections() {
        val stops = uiState.value.routeStops.map { it.toRouteStopCreation() }
        getDirectionsUseCase(stops).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val polylines = PolylineOptions()
                    result.data?.let { polylines.addAll(it.points) }
                    _uiState.update { it.copy(polylineOptions = polylines) }
                }
                is DataState.Error -> _uiState.update {
                    it.copy(errorMessage = result.error)
                }
                is DataState.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun setFromGpx(gpx: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uri = gpx.toInputStream()
                val parsedGpx: Gpx? = gpxParser.parse(uri.getOrThrow()!!)
                parsedGpx?.let { gpx ->
                    val stops = gpx.wayPoints.mapIndexed { index, value ->
                        NewRouteStop(index + 1, value.latitude, value.longitude)
                    }
                    resetStops(stops)
                }
            } catch (e: IOException) {
                Log.e("GPX Parse IO ERROR", e.localizedMessage, e)
            } catch (e: XmlPullParserException) {
                Log.e("GPX Parse XML", e.localizedMessage, e)
            }
        }
    }

    fun resetStops(stops: List<NewRouteStop>) {
        _uiState.update {
            it.copy(routeStops = stops, isLoadedFromGPX = true)
        }
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
        createRouteUseCase(uiState.value.toRouteCreation()).onEach { result ->
            when (result) {
                is DataState.Success -> _uiState.update {
                    it.copy(
                        isInserted = true,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is DataState.Loading -> _uiState.update {
                    it.copy(isLoading = true)
                }
                is DataState.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error)
                }
            }
        }.launchIn(viewModelScope)
    }
}

