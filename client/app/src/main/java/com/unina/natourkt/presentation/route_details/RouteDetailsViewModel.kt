package com.unina.natourkt.presentation.route_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.common.DataState
import com.unina.natourkt.domain.model.route.toDetailUi
import com.unina.natourkt.domain.model.toDetailUi
import com.unina.natourkt.domain.model.toGridUi
import com.unina.natourkt.domain.model.toUi
import com.unina.natourkt.domain.use_case.maps.GetDirectionsUseCase
import com.unina.natourkt.domain.use_case.post.GetTaggedPostsUseCase
import com.unina.natourkt.domain.use_case.route.GetRouteDetailsUseCase
import com.unina.natourkt.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.presentation.base.ui_state.PostGridItemUiState
import com.unina.natourkt.presentation.base.ui_state.convertKeys
import com.unina.natourkt.presentation.new_route.toRouteStopCreation
import com.unina.natourkt.presentation.post_details.convertKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val getRouteDetailsUseCase: GetRouteDetailsUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getTaggedPostsUseCase: GetTaggedPostsUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    val routeId = savedState.get<Long>("routeId")
    val authorId = savedState.get<Long>("authorId")

    private val _uiState = MutableStateFlow(RouteDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostGridItemUiState>>
    val postsFlow: Flow<PagingData<PostGridItemUiState>>
        get() = _postsFlow

    init {
        getLoggedUser()
        getRouteDetails(routeId!!)
        getTaggedPosts()
    }

    private fun getRouteDetails(id: Long) {
        getRouteDetailsUseCase(id).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            route = result.data?.toDetailUi()?.convertKeys {
                                getUrlFromKeyUseCase(it)
                            })
                    }
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.error)
                    }
                }

                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getLoggedUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loggedUser = getUserDataUseCase()!!.toUi())
            }
        }
    }

    fun getDirections() {
        val stops = uiState.value.route?.stops?.map { it.toRouteStop() }
        getDirectionsUseCase(stops!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val polylines = PolylineOptions()
                    result.data?.let { polylines.addAll(it.points) }
                    val oldRoute = uiState.value.route
                    val newRoute = oldRoute?.copy(polylineOptions = polylines)
                    _uiState.update { it.copy(route = newRoute) }
                }
                is DataState.Error -> _uiState.update {
                    it.copy(error = result.error)
                }
                is DataState.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getTaggedPosts() {
        viewModelScope.launch {
            _postsFlow = getTaggedPostsUseCase(routeId!!)
                .map { pagingData ->
                    pagingData.map { post ->
                        post.toGridUi().convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}