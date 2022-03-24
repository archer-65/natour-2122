package com.unina.natourkt.feature_post.post_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.use_case.chat.GetChatByMembersUseCase
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.domain.use_case.post.GetPostDetailsUseCase
import com.unina.natourkt.core.domain.use_case.storage.GetUrlFromKeyUseCase
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.mapper.PostDetailsUiMapper
import com.unina.natourkt.core.presentation.model.mapper.UserUiMapper
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val getPostDetailsUseCase: GetPostDetailsUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUrlFromKeyUseCase: GetUrlFromKeyUseCase,
    private val getChatByMembersUseCase: GetChatByMembersUseCase,
    private val postDetailsUiMapper: PostDetailsUiMapper,
    private val userUiMapper: UserUiMapper,
    savedState: SavedStateHandle
) : ViewModel() {

    private val postId = savedState.get<Long>("postId")

    private val _uiState = MutableStateFlow(PostDetailsUiState())
    val uiState: StateFlow<PostDetailsUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLoggedUser()
        getPostDetails()
    }

    fun onEvent(event: PostDetailsEvent) {
        when (event) {
            PostDetailsEvent.ResetChat -> resetChat()
            PostDetailsEvent.ShowChat -> getChat()
        }
    }

    private fun getPostDetails() {
        getPostDetailsUseCase(postId!!).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val postUi = result.data?.let { postDetailsUiMapper.mapToUi(it) }
                    val postDetails = postUi?.convertKeys { getUrlFromKeyUseCase(it) }

                    _uiState.update {
                        it.copy(isLoading = false, isError = false, post = postDetails)
                    }
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

    private fun getChat() {
        viewModelScope.launch {
            getChatByMembersUseCase(
                uiState.value.loggedUser!!.id,
                uiState.value.post!!.authorId
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