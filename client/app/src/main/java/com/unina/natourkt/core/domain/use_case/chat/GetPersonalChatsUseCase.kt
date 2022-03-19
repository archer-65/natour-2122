package com.unina.natourkt.core.domain.use_case.chat

import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonalChatsUseCase @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val chatRepository: ChatRepository,
) {

    suspend operator fun invoke(): Flow<PagingData<Chat>> {
        //Log.i(Constants.COMPILATION_MODEL, "Getting paginated compilations for user...")
        val loggedUser = getUserDataUseCase()

        return chatRepository.getPersonalChats(loggedUser!!.id)
    }
}