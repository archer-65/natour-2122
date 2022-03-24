package com.unina.natourkt.feature_chat.chat_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.use_case.chat.GetChatByMembersUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.domain.use_case.user.GetUsersUseCase
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ChatSearchViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val getChatByMembersUseCase: GetChatByMembersUseCase,
    private val userUiMapper: UserUiMapper,
    savedState: SavedStateHandle
) : ViewModel() {

    val loggedUserId = savedState.get<Long>("userToExcludeId")!!

    private lateinit var _usersResult: Flow<PagingData<UserUi>>
    val usersResult
        get() = _usersResult

    private val _uiState = MutableStateFlow(ChatSearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getResults()
    }

    fun onEvent(event: ChatSearchEvent) {
        when (event) {
            is ChatSearchEvent.EnteredQuery -> setQuery(event.query)
            is ChatSearchEvent.ShowChat -> getChat(event.userId)
            ChatSearchEvent.ResetChat -> resetChat()
        }
    }

    private fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    private fun getResults() {
        _usersResult = _uiState.filter { it.query.isNotBlank() }.flatMapLatest { filter ->
            getUsersUseCase(filter.query, loggedUserId)
                .map { pagingData ->
                    pagingData.map { user ->
                        val userUi = userUiMapper.mapToUi(user)
                        userUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }.cachedIn(viewModelScope)
        }
    }

    private fun getChat(userId: Long) {
        viewModelScope.launch {
            getChatByMembersUseCase(
                loggedUserId,
                userId
            ).onEach { result ->
                when (result) {
                    is DataState.Success -> {
                        val chatUi = result.data?.mapToUi()
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
        val user = if (loggedUserId == this.firstMember.id) {
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