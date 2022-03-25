package com.unina.natourkt.core.domain.use_case.chat

import android.util.Log
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve a certain [Chat] between two users
 * @see [ChatRepository]
 */
class GetChatByMembersUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {

    operator fun invoke(firstMemberId: Long, secondMemberId: Long): Flow<DataState<Chat>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.CHAT_MODEL,
            "Getting messages for chat with users: ${firstMemberId} and ${secondMemberId}..."
        )

        val result = chatRepository.getChatByMembers(firstMemberId, secondMemberId)
        emit(result)
    }
}