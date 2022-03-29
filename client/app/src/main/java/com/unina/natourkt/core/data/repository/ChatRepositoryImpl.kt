package com.unina.natourkt.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.unina.natourkt.core.data.paging.PersonalChatsSource
import com.unina.natourkt.core.data.remote.dto.mapper.ChatApiMapper
import com.unina.natourkt.core.data.remote.dto.mapper.MessageApiMapper
import com.unina.natourkt.core.data.remote.retrofit.ChatApi
import com.unina.natourkt.core.data.util.retrofitSafeCall
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.model.Message
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This implementation of [ChatRepository] works with a [ChatApi] Retrofit interface
 * It also contains mappers for model conversions
 * @see [ChatApiMapper]
 * @see [MessageApiMapper]
 */
class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi,
    private val chatApiMapper: ChatApiMapper,
    private val messageApiMapper: MessageApiMapper,
) : ChatRepository {

    /**
     * Page size for network requests for this class
     * NOTE: The first page defaults to pageSize * 3!
     */
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    override fun getPersonalChats(userId: Long): Flow<PagingData<Chat>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PersonalChatsSource(
                    api,
                    chatApiMapper,
                    userId
                )
            },
        ).flow
    }

    override suspend fun getChatByMembers(
        firstMemberId: Long,
        secondMemberId: Long
    ): DataState<Chat> =
        retrofitSafeCall(IO) {
            val response = api.getChatByMembers(firstMemberId, secondMemberId)
            chatApiMapper.mapToDomain(response)
        }

    override suspend fun getChatMessages(chatId: Long): DataState<List<Message>> {
        return retrofitSafeCall(IO) {
            val messagesResult = api.getChatMessages(chatId)
            messagesResult.map { messageApiMapper.mapToDomain(it) }
        }
    }
}