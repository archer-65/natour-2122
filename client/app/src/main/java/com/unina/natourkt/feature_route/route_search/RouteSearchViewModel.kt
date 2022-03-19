package com.unina.natourkt.feature_route.route_search

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.core.domain.use_case.route.GetFilteredRoutesUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.model.mapper.RouteItemUiMapper
import com.unina.natourkt.core.util.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RouteSearchViewModel @Inject constructor(
    private val getFilteredRoutesUseCase: GetFilteredRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val routeItemUiMapper: RouteItemUiMapper,
) : ViewModel() {

    private lateinit var _routeResults: Flow<PagingData<RouteItemUi>>
    val routeResults
        get() = _routeResults

    private val _uiState = MutableStateFlow(RouteSearchUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RouteSearchEvent) {
        when (event) {
            is RouteSearchEvent.EnteredQuery -> setQuery(event.query)
            is RouteSearchEvent.FilterPlace -> setPlace(event.place)
            is RouteSearchEvent.FilterDistance -> setDistance(event.distance)
            is RouteSearchEvent.FilterDuration -> setDurationRange(
                event.minDuration,
                event.maxDuration
            )
            is RouteSearchEvent.FilterDifficulty -> setDifficulty(event.difficulty)
            is RouteSearchEvent.FilterDisability -> setDisability(event.disability)
        }
    }

    init {
        getResults()
    }

    private fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    private fun setPlace(place: Place?) {
        _uiState.update {
            it.copy(place = place)
        }
    }

    private fun setDistance(distance: Float) {
        _uiState.update {
            it.copy(distance = distance)
        }
    }

    private fun setDurationRange(minDuration: Int?, maxDuration: Int?) {
        _uiState.update {
            it.copy(minDuration = minDuration, maxDuration = maxDuration)
        }
    }

    private fun setDifficulty(difficulty: Difficulty) {
        _uiState.update {
            it.copy(minDifficulty = difficulty)
        }
    }

    private fun setDisability(isDisabilityFriendly: Boolean?) {
        _uiState.update {
            it.copy(isDisabilityFriendly = isDisabilityFriendly)
        }
    }

    private fun getResults() {
        _routeResults = _uiState.filter { it.query.isNotBlank() }.flatMapLatest { filter ->
            getFilteredRoutesUseCase(filter.toFilter())
                .map { pagingData ->
                    pagingData.map { route ->
                        val routeUi = routeItemUiMapper.mapToUi(route)
                        routeUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }.cachedIn(viewModelScope)
        }
    }
}