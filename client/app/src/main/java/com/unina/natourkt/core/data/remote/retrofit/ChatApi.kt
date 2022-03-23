package com.unina.natourkt.core.data.remote.retrofit

import com.unina.natourkt.core.data.remote.dto.ChatDto
import com.unina.natourkt.core.data.remote.dto.MessageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @GET("/chats/search_page")
    suspend fun getChatsByUser(
        @Query("user_id") userId: Long,
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): List<ChatDto>

    @GET("/chat/search")
    suspend fun getChatByMembers(
        @Query("sender_id") firstMemberId: Long,
        @Query("recipient_id") secondMemberId: Long
    ): ChatDto


    @GET("/chats/{id}/messages")
    suspend fun getChatMessages(
        @Path("id") chatId: Long,
    ): List<MessageDto>
}