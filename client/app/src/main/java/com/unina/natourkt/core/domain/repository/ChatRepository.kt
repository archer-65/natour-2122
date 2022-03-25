package com.unina.natourkt.core.domain.repository

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Interface for chat and chat messages retrieval and creation functions repository
 */
interface ChatRepository {

    /**
     * This function gets all chats for a certain user
     */
    fun getPersonalChats(userId: Long): Flow<PagingData<Chat>>

    /**
     * This function gets all chat messages for a certain chat
     */
    suspend fun getChatMessages(chatId: Long): DataState<List<Message>>

    /**
     * This function gets a chat given the ids of its members
     */
    suspend fun getChatByMembers(firstMemberId: Long, secondMemberId: Long): DataState<Chat>
}