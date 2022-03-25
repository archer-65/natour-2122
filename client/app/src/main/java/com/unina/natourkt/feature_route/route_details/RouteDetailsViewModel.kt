package com.unina.natourkt.feature_route.route_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.core.analytics.ActionEvents
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.use_case.analytics.ActionAnalyticsUseCase
import com.unina.natourkt.core.domain.use_case.chat.GetChatByMembersUseCase
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.maps.GetDirectionsUseCase
import com.unina.natourkt.core.domain.use_case.post.GetTaggedPostsUseCase
import com.unina.natourkt.core.domain.use_case.route.GetRouteDetailsUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.PostGridItemUi
import com.unina.natourkt.core.presentation.model.mapper.PostGridItemUiMapper
import com.unina.natourkt.core.presentation.model.mapper.RouteDetailsUiMapper
import com.unina.natourkt.core.presentation.model.mapper.RouteStopUiMapper
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
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
    private val getChatByMembersUseCase: GetChatByMembersUseCase,
    private val analytics: ActionAnalyticsUseCase,
    private val routeDetailsUiMapper: RouteDetailsUiMapper,
    private val routeStopUiMapper: RouteStopUiMapper,
    private val postGridItemUiMapper: PostGridItemUiMapper,
    private val userUiMapper: UserUiMapper,
    savedState: SavedStateHandle
) : ViewModel() {

    val routeId = savedState.get<Long>("routeId")!!

    private val _uiState = MutableStateFlow(RouteDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _postsFlow: Flow<PagingData<PostGridItemUi>>
    val postsFlow: Flow<PagingData<PostGridItemUi>>
        get() = _postsFlow

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RouteDetailsEvent) {
        when (event) {
            RouteDetailsEvent.ResetChat -> resetChat()
            RouteDetailsEvent.ShowChat -> getChat()
            RouteDetailsEvent.ClickPost -> analytics.sendEvent(ActionEvents.ClickPost)
        }
    }

    init {
        getLoggedUser()
        getRouteDetails()
        getTaggedPosts()
    }

    private fun getRouteDetails() {
        getRouteDetailsUseCase(routeId).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val routeUi = result.data?.let { routeDetailsUiMapper.mapToUi(it) }
                    val routeDetails = routeUi?.convertKeys { getUrlFromKeyUseCase(it) }

                    _uiState.update {
                        it.copy(isLoading = false, isError = false, route = routeDetails)
                    }

                    getDirections()
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, isError = true)
                    }

                    val errorText = UiTextCauseMapper.mapToText(result.error)
                    _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                }

                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true, isError = false)
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

    private fun getDirections() {
        val stops = uiState.value.route?.stops?.map { routeStopUiMapper.mapToDomain(it) }
        if (stops!!.isNotEmpty()) {
            getDirectionsUseCase(stops).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        val polylines = PolylineOptions()
                        result.data?.let { polylines.addAll(it.points) }
                        _uiState.update { it.copy(polylineOptions = polylines) }
                    }
                    is DataState.Error -> {
                        val errorText = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(errorText))
                    }
                    is DataState.Loading -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getTaggedPosts() {
        viewModelScope.launch {
            _postsFlow = getTaggedPostsUseCase(routeId)
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

    private fun getChat() {
        viewModelScope.launch {
            getChatByMembersUseCase(
                uiState.value.loggedUser!!.id,
                uiState.value.route?.author!!.id
            ).onEach { result ->

                when (result) {
                    is DataState.Success -> {
                        val chatUi = result.data?.mapToUi()

                        analytics.sendEvent(ActionEvents.ClickChat)
                        _uiState.update { it.copy(isLoading = false, retrievedChat = chatUi) }
                    }
                    is DataState.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is DataState.Error -> {
                        _uiState.update { it.copy(isLoading = false) }

                        val text = UiTextCauseMapper.mapToText(result.error)
                        _eventFlow.emit(UiEffect.ShowSnackbar(text))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun resetChat() {
        _uiState.update { it.copy(retrievedChat = null) }
    }

    private fun Chat.mapToUi(): ChatItemUi {
        val user = if (uiState.value.loggedUser?.id == this.firstMember.id) {
            this.secondMember
        } else {
            this.firstMember
        }

        return ChatItemUi(
            chatId = id,
            otherMemberId = user.id,
            otherMemberUsername = user.username,
            otherMemberPhoto = user.profilePhoto ?: "",
        )
    }
}