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

        if (firstMemberId <= 0L || secondMemberId <= 0L) {
            emit(DataState.Error(DataState.Cause.NotAcceptable))
            return@flow
        }
        if (firstMemberId == secondMemberId) {
            emit(DataState.Error(DataState.Cause.BadRequest))
            return@flow
        }

        val result = chatRepository.getChatByMembers(firstMemberId, secondMemberId)
        val chat = result.data

        if (
            (chat?.firstMember?.id == firstMemberId && chat.secondMember.id == secondMemberId)
            ||
            (chat?.firstMember?.id == secondMemberId && chat.secondMember.id == firstMemberId)
        ) {
            emit(result)
        } else {
            emit(DataState.Error(DataState.Cause.NotFound))
        }
    }
}