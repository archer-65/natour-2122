package com.unina.natourkt.core.domain.use_case.chat

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve a certain user's chat history
 * @see [ChatRepository]
 * @see [GetUserDataUseCase]
 */
class GetPersonalChatsUseCase @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val chatRepository: ChatRepository,
) {

    suspend operator fun invoke(): Flow<PagingData<Chat>> {
        val loggedUser = getUserDataUseCase()
        Log.i(Constants.CHAT_MODEL, "Getting chat for logged user ${loggedUser?.username}...")

        return chatRepository.getPersonalChats(loggedUser!!.id)
    }
}