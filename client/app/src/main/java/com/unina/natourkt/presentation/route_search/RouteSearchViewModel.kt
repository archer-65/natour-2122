package com.unina.natourkt.presentation.route_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.domain.model.route.toUi
import com.unina.natourkt.domain.use_case.route.GetFilteredRoutesUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.model.RouteItemUiState
import com.unina.natourkt.presentation.base.model.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RouteSearchViewModel @Inject constructor(
    private val getFilteredRoutesUseCase: GetFilteredRoutesUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase
) : ViewModel() {

    private lateinit var _routeResults: Flow<PagingData<RouteItemUiState>>
    val routeResults
        get() = _routeResults

    private val _uiState = MutableStateFlow(RouteSearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getResults()
    }

    fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    fun setPlace(place: Place?) {
        _uiState.update {
            it.copy(place = place)
        }
    }

    fun setDistance(distance: Float) {
        _uiState.update {
            it.copy(distance = distance)
        }
    }

    fun setDurationRange(minDuration: Int?, maxDuration: Int?) {
        _uiState.update {
            it.copy(minDuration = minDuration, maxDuration = maxDuration)
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update {
            it.copy(minDifficulty = difficulty)
        }
    }

    fun setDisability(isDisabilityFriendly: Boolean?) {
        _uiState.update {
            it.copy(isDisabilityFriendly = isDisabilityFriendly)
        }
    }

    fun getResults() {
        _routeResults = _uiState.flatMapLatest { filter ->
            getFilteredRoutesUseCase(filter.toFilter())
                .map { pagingData ->
                    pagingData.map { route ->
                        route.toUi().convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }.cachedIn(viewModelScope)
        }
    }

//    val _routeResults =
//        _uiState.flatMapLatest { filter ->
//            getFilteredRoutesUseCase(filter.toFilter())
//                .map { pagingData ->
//                    pagingData.map { route ->
//                        route.toUi().convertKeys {
//                            getUrlFromKeyUseCase(it)
//                        }
//                    }
//                }.cachedIn(viewModelScope)
//        }
}