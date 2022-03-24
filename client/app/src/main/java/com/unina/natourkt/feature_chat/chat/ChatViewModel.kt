package com.unina.natourkt.feature_chat.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unina.natourkt.core.data.remote.dto.MessageCreationDto
import com.unina.natourkt.core.data.remote.dto.MessageDto
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.domain.use_case.chat.GetChatMessagesUseCase
import com.unina.natourkt.core.presentation.model.ChatItemUi
import com.unina.natourkt.core.presentation.model.MessageItemUi
import com.unina.natourkt.core.presentation.model.MessageType
import com.unina.natourkt.core.presentation.model.groupIntoMap
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.UiTextCauseMapper
import com.unina.natourkt.core.util.Constants.BASE_WS
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.DateTimeParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.*
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val client: StompClient,
    savedState: SavedStateHandle
) : ViewModel() {

    private lateinit var session: StompSession
    private lateinit var jsonStompSession: StompSessionWithKxSerialization

    private val chatInfo = savedState.get<ChatItemUi>("chatInfo")
    private val userId = savedState.get<Long>("userMeId")

    private val _uiState = MutableStateFlow(ChatUiState(chatInfo = chatInfo!!))
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getMessages()
        collector()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            ChatEvent.ReadAll -> setReadMessages()
            ChatEvent.SendMessage -> sender()
            is ChatEvent.UpdateMessage -> messageUpdate(event.body)
        }
    }

    private fun getMessages() {
        getChatMessagesUseCase(uiState.value.chatInfo.chatId).onEach { result ->
            when (result) {
                is DataState.Success -> {
                    val messages = result.data ?: emptyList()
                    val messagesUi = messages.map {
                        it.mapToUi()
                    }.groupIntoMap()

                    _uiState.update {
                        it.copy(messages = messagesUi, isLoading = false)
                    }
                }
                is DataState.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
                is DataState.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun messageUpdate(message: String) {
        _uiState.update {
            it.copy(messageState = message)
        }
    }

    private fun setReadMessages() {
        _uiState.update {
            it.copy(newMessagesNumber = 0)
        }
    }

    private fun sender() {
        viewModelScope.launch {
            try {
                session = client.connect(url = BASE_WS)
                jsonStompSession = session.withJsonConversions()

                _uiState.update { it.copy(shouldScrollToBottom = false) }
                jsonStompSession.use { session ->
                    val header = StompSendHeaders("/natour/chat")
                    session.convertAndSend(
                        header,
                        MessageCreationDto(
                            chatId = uiState.value.chatInfo.chatId,
                            recipientId = uiState.value.chatInfo.otherMemberId,
                            senderId = userId!!,
                            messageContent = uiState.value.messageState
                        ),
                        MessageCreationDto.serializer()
                    )
                }
                _uiState.update { it.copy(messageState = "", shouldScrollToBottom = true) }
            } catch (stompException: ConnectionException) {
                val text = UiTextCauseMapper.mapToText(DataState.Cause.NetworkError)
                _eventFlow.emit(UiEvent.ShowToast(text))
            }
        }
    }

    private fun collector() {
        viewModelScope.launch {
            try {
                session = client.connect(url = BASE_WS)
                jsonStompSession = session.withJsonConversions()

                val messages: Flow<MessageDto> = jsonStompSession.subscribe(
                    "/user/${userId}/queue/messages",
                    MessageDto.serializer()
                )

                messages.collect { msg ->
                    val mex = Message(
                        id = msg.messageId,
                        content = msg.messageContent,
                        senderId = msg.senderId,
                        recipientId = msg.recipientId,
                        chatId = msg.chatId,
                        sentOn = DateTimeParser.parseDateTime(msg.sentOn)
                    )

                    _uiState.update {
                        val oldMessages = it.messages.toMutableList()
                        oldMessages.add(index = 0, element = mex.mapToUi())

                        it.copy(
                            messages = oldMessages.toList(),
                            newMessagesNumber = it.newMessagesNumber + 1
                        )
                    }
                }
            } catch (stompException: ConnectionException) {
                val text = UiTextCauseMapper.mapToText(DataState.Cause.NetworkError)
                _eventFlow.emit(UiEvent.ShowToast(text))
            }
        }
    }

    private fun Message.mapToUi(): MessageItemUi {
        val type = when (this.senderId) {
            userId -> MessageType.TYPE_ME
            else -> MessageType.TYPE_OTHER
        }

        return MessageItemUi(
            messageId = this.id,
            type = type,
            content = this.content,
            sentOn = this.sentOn,
        )
    }
}