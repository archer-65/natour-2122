package com.unina.natourkt.core.domain.use_case.chat

import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatByMembersUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {

    operator fun invoke(firstMemberId: Long, secondMemberId: Long): Flow<DataState<Chat>> = flow {
        emit(DataState.Loading())

        val result = chatRepository.getChatByMembers(firstMemberId, secondMemberId)
        emit(result)
    }
}