package com.unina.natourkt.core.domain.use_case.chat

import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {

    operator fun invoke(chatId: Long): Flow<DataState<List<Message>>> = flow {
        emit(DataState.Loading())

        val result = chatRepository.getChatMessages(chatId)
        emit(result)
    }
}