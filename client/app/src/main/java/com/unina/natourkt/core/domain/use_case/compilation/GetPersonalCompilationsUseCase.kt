package com.unina.natourkt.core.domain.use_case.compilation

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.domain.repository.CompilationRepository
import com.unina.natourkt.core.domain.use_case.settings.GetUserDataUseCase
import com.unina.natourkt.core.util.Constants.COMPILATION_MODEL
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This UseCase is used to retrieve personal compilations
 * @see [CompilationRepository]
 * @see [GetUserDataUseCase]
 */
class GetPersonalCompilationsUseCase @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val compilationRepository: CompilationRepository,
) {

    suspend operator fun invoke(): Flow<PagingData<Compilation>> {
        val loggedUser = getUserDataUseCase()
        Log.i(COMPILATION_MODEL, "Getting compilations for user: ${loggedUser?.username}...")

        return compilationRepository.getPersonalCompilations(loggedUser!!.id)
    }
}