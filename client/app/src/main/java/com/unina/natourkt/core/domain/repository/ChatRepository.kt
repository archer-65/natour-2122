package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getPersonalChats(userId: Long): Flow<PagingData<Chat>>
    suspend fun getChatMessages(chatId: Long): DataState<List<Message>>
    suspend fun getChatByMembers(firstMemberId: Long, secondMemberId: Long): DataState<Chat>
}