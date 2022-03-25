package com.unina.natourkt.core.domain.use_case.chat

import android.util.Log
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve all chat's messages
 * @see [ChatRepository]
 */
class GetChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {

    operator fun invoke(chatId: Long): Flow<DataState<List<Message>>> = flow {
        emit(DataState.Loading())
        Log.i(Constants.CHAT_MODEL, "Getting messages for chat with ID: ${chatId}...")

        val result = chatRepository.getChatMessages(chatId)
        emit(result)
    }
}