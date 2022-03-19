package com.unina.natourkt.feature_chat.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.unina.natourkt.core.data.remote.dto.MessageCreationDto
import com.unina.natourkt.core.data.remote.dto.MessageDto
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.domain.use_case.chat.GetChatMessagesUseCase
import com.unina.natourkt.core.presentation.model.*
import com.unina.natourkt.core.presentation.util.format
import com.unina.natourkt.core.util.Constants.BASE_WS
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.DateTimeParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.use
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

    init {
        getMessages()
        connectToWs()
        collector()
    }

    fun getMessages() {
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

    fun connectToWs() {
        viewModelScope.launch {
            session = client.connect(url = BASE_WS)
            jsonStompSession = session.withJsonConversions()
        }
    }

    fun sender() {
        viewModelScope.launch {
            session = client.connect(url = BASE_WS)
            jsonStompSession = session.withJsonConversions()

            jsonStompSession.use { session ->
                val header = StompSendHeaders("/natour/chat")
                val receipt = session.convertAndSend(
                    header,
                    MessageCreationDto(
                        chatId = uiState.value.chatInfo.chatId,
                        recipientId = uiState.value.chatInfo.otherMemberId,
                        senderId = userId!!,
                        messageContent = "Text example"
                    ),
                    MessageCreationDto.serializer()
                )

                receipt?.let {

                }
            }
        }
    }

    fun collector() {
        viewModelScope.launch {
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

                    it.copy(messages = oldMessages.toList())
                }
            }
        }
    }

    fun Message.mapToUi(): MessageItemUi {
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

    fun List<MessageItemUi>.groupIntoMap(): List<ChatGenericUi> {
        val groupedHashMap: LinkedHashMap<String, MutableSet<MessageItemUi>> = LinkedHashMap()
        var list: MutableSet<MessageItemUi>

        for (message in this) {
            val hashMapKey: String = message.sentOn.toLocalDate().toString()
            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey)?.add(message)
            } else {
                list = LinkedHashSet()
                list.add(message)
                groupedHashMap.put(hashMapKey, list)
            }
        }

        return groupedHashMap.generateListFromMap()
    }

    fun LinkedHashMap<String, MutableSet<MessageItemUi>>.generateListFromMap(): List<ChatGenericUi> {
        val newList: MutableList<ChatGenericUi> = ArrayList()
        for (date in this.keys) {
            val dateItem =
                DateItemUi(date = DateTimeParser.parseDate(date), type = MessageType.TYPE_DATE)

            val set = this.get(date)
            if (set != null) {
                for (message in set) {
                    newList.add(message)
                }
            }
            newList.add(dateItem)
        }

        return newList.toList()
    }

}