package com.unina.natourkt.feature_route.route_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.maps.GetDirectionsUseCase
import com.unina.natourkt.core.domain.use_case.post.GetTaggedPostsUseCase
import com.unina.natourkt.core.domain.use_case.route.GetRouteDetailsUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.PostGridItemUi
import com.unina.natourkt.core.presentation.model.mapper.PostGridItemUiMapper
import com.unina.natourkt.core.presentation.model.mapper.RouteDetailsUiMapper
import com.unina.natourkt.core.presentation.model.mapper.RouteStopUiMapper
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
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
    private val routeDetailsUiMapper: RouteDetailsUiMapper,
    private val routeStopUiMapper: RouteStopUiMapper,
    private val postGridItemUiMapper: PostGridItemUiMapper,
    private val userUiMapper: UserUiMapper,
    savedState: SavedStateHandle
) : ViewModel() {

    val routeId = savedState.get<Long>("routeId")
    val authorId = savedState.get<Long>("authorId")

    private val _uiState = MutableStateFlow(RouteDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostGridItemUi>>
    val postsFlow: Flow<PagingData<PostGridItemUi>>
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
                        val routeUi = result.data?.let { routeDetailsUiMapper.mapToUi(it) }
                        it.copy(
                            isLoading = false,
                            error = null,
                            route = routeUi?.convertKeys {
                                getUrlFromKeyUseCase(it)
                            })
                    }
                    getDirections()
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
                val userUi = userUiMapper.mapToUi(getUserDataUseCase()!!)
                it.copy(loggedUser = userUi)
            }
        }
    }

    fun getDirections() {
        val stops = uiState.value.route?.stops?.map { routeStopUiMapper.mapToDomain(it) }
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
                        val postUi = postGridItemUiMapper.mapToUi(post)
                        postUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }
}