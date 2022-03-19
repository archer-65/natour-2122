package com.unina.natourkt.feature_chat.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.use_case.chat.GetPersonalChatsUseCase
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getPersonalChatsUseCase: GetPersonalChatsUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val userUiMapper: UserUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var _chatsFlow: Flow<PagingData<ChatItemUi>>
    val chatsFlow: Flow<PagingData<ChatItemUi>>
        get() = _chatsFlow

    init {
        getLoggedUser()
        getChats()
    }

    fun getLoggedUser() {
        viewModelScope.launch {
            getUserDataUseCase()?.let {
                val userUi = userUiMapper.mapToUi(it).convertKeys {
                    getUrlFromKeyUseCase(it)
                }
                _uiState.update {
                    it.copy(loggedUser = userUi)
                }
            }
        }
    }

    fun getChats() {
        viewModelScope.launch {
            _chatsFlow = getPersonalChatsUseCase()
                .map { pagingData ->
                    pagingData.map { chat ->
                        val chatUi = chat.mapToUi()
                        chatUi.convertKeys {
                            getUrlFromKeyUseCase(it)
                        }
                    }
                }
                .cachedIn(viewModelScope)
        }
    }

    fun Chat.mapToUi(): ChatItemUi {
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