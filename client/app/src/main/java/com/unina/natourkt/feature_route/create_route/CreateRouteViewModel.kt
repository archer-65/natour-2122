package com.unina.natourkt.feature_route.create_route

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.model.RouteCreation
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.maps.GetDirectionsUseCase
import com.unina.natourkt.core.domain.use_case.route.CreateRouteUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.presentation.model.RouteStopUi
import com.unina.natourkt.core.presentation.model.mapper.RouteStopUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.core.util.safeRemove
import com.unina.natourkt.core.util.toInputStream
import com.unina.natourkt.feature_route.create_route.info.CreateRouteInfoUiState
import com.unina.natourkt.feature_route.create_route.map.CreateRouteMapUiState
import com.unina.natourkt.feature_route.create_route.photos.CreateRoutePhotosUiState
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
class CreateRouteViewModel @Inject constructor(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val analyticsUseCase: ActionAnalyticsUseCase,
    private val gpxParser: GPXParser,
    private val routeStopUiMapper: RouteStopUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRouteUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiStateInfo = MutableStateFlow(CreateRouteInfoUiState())
    val uiStateInfo = _uiStateInfo.asStateFlow()

    private val _uiStateMap = MutableStateFlow(CreateRouteMapUiState())
    val uiStateMap = _uiStateMap.asStateFlow()

    private val _uiStatePhotos = MutableStateFlow(CreateRoutePhotosUiState())
    val uiStatePhotos = _uiStatePhotos.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreateRouteEvent) {
        when (event) {
            // INFO
            is CreateRouteEvent.EnteredTitle -> setTitle(event.title)
            is CreateRouteEvent.EnteredDescription -> setDescription(event.description)
            is CreateRouteEvent.EnteredDifficulty -> setDifficulty(event.difficulty)
            is CreateRouteEvent.EnteredDisability -> setDisabilityFriendly(event.disability)
            is CreateRouteEvent.EnteredDuration -> setDuration(event.duration)

            // MAP
            is CreateRouteEvent.AddedStop -> addStop(event.latitude, event.longitude)
            is CreateRouteEvent.InsertedGpx -> setFromGpx(event.gpx)
            is CreateRouteEvent.CleanStop -> cleanStops()

            // PHOTOS
            is CreateRouteEvent.InsertedPhotos -> setPhotos(event.photos)
            is CreateRouteEvent.RemovePhoto -> removePhoto(event.position)

            // GENERAL
            is CreateRouteEvent.Upload -> uploadRoute()

            CreateRouteEvent.SearchPlace -> analyticsUseCase.sendEvent(ActionEvents.SearchPlace)
            CreateRouteEvent.SelectGpx -> analyticsUseCase.sendEvent(ActionEvents.SelectGpx)
        }
    }

    private fun setTitle(title: String) {
        _uiStateInfo.update {
            it.copy(routeTitle = it.routeTitle.copy(text = title))
        }
    }

    private fun setDescription(description: String) {
        _uiStateInfo.update {
            it.copy(routeDescription = it.routeDescription.copy(text = description))
        }
    }

    private fun setDuration(duration: String) {
        _uiStateInfo.update {
            it.copy(duration = it.duration.copy(text = duration))
        }
    }

    private fun setDisabilityFriendly(checked: Boolean) {
        _uiStateInfo.update {
            it.copy(disabilityFriendly = checked)
        }
    }

    private fun setDifficulty(difficulty: Difficulty) {
        _uiStateInfo.update {
            it.copy(difficulty = difficulty)
        }
    }

    private fun addStop(latitude: Double, longitude: Double) {
        analyticsUseCase.sendEvent(ActionEvents.AddMarker)

        _uiStateMap.update {
            val newStops = it.stops + RouteStopUi(
                stopNumber = it.stops.size + 1,
                latitude,
                longitude
            )
            it.copy(stops = newStops, isLoadedFromGPX = false)
        }

        if (uiStateMap.value.shouldGetDirections) getDirections()
    }

    private fun cleanStops() {
        analyticsUseCase.sendEvent(ActionEvents.CleanMap)
        _uiStateMap.update {
            it.copy(stops = emptyList())
        }
    }

    private fun getDirections() {
        val stops = uiStateMap.value.stops.map { routeStopUiMapper.mapToDomain(it) }
        getDirectionsUseCase(stops).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val polylines = PolylineOptions()
                    result.data?.let { polylines.addAll(it.points) }
                    _uiStateMap.update { it.copy(polylineOptions = polylines) }
                }
                is DataState.Error -> {
                    val errorText = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                }
                is DataState.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }


    private fun setFromGpx(gpx: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uri = gpx.toInputStream()
                val parsedGpx: Gpx? = gpxParser.parse(uri.getOrThrow()!!)
                parsedGpx?.let { gpx ->
                    val stops = gpx.wayPoints.mapIndexed { index, value ->
                        RouteStopUi(index + 1, value.latitude, value.longitude)
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

    private fun resetStops(stops: List<RouteStopUi>) {
        _uiStateMap.update {
            it.copy(stops = stops, isLoadedFromGPX = true)
        }
    }

    private fun setPhotos(photos: List<Uri>) {
        _uiStatePhotos.update {
            it.copy(photos = photos)
        }
    }

    private fun removePhoto(position: Int) {
        _uiStatePhotos.update {
            it.copy(photos = it.photos.safeRemove(position))
        }
    }

    private fun uploadRoute() {
        viewModelScope.launch {
            createRouteUseCase(mapForCreation()).onEach { result ->
                when (result) {
                    is DataState.Success -> _uiState.update {
                        analyticsUseCase.sendEvent(ActionEvents.CreateRoute)

                        it.copy(isInserted = true, isLoading = false)
                    }
                    is DataState.Loading -> _uiState.update {
                        it.copy(isLoading = true)
                    }
                    is DataState.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun mapForCreation(): RouteCreation {
        return RouteCreation(
            title = uiStateInfo.value.routeTitle.text,
            description = uiStateInfo.value.routeDescription.text,
            avgDifficulty = uiStateInfo.value.difficulty,
            avgDuration = uiStateInfo.value.duration.text.toDouble(),
            disabilityFriendly = uiStateInfo.value.disabilityFriendly,
            photos = uiStatePhotos.value.photos.map { it.toString() },
            stops = uiStateMap.value.stops.map { routeStopUiMapper.mapToDomain(it) },
            author = getUserDataUseCase()
        )
    }
}

