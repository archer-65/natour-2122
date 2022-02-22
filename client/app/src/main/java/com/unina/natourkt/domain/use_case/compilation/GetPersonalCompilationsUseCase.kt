package com.unina.natourkt.domain.use_case.compilation

import android.util.Log
import androidx.paging.PagingData
import com.unina.natourkt.common.Constants.COMPILATION_MODEL
import com.unina.natourkt.domain.model.Compilation
import com.unina.natourkt.domain.repository.CompilationRepository
import com.unina.natourkt.domain.repository.RouteRepository
import com.unina.natourkt.domain.use_case.datastore.GetUserFromStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonalCompilationsUseCase @Inject constructor(

    private val getUserFromStoreUseCase: GetUserFromStoreUseCase,
    private val compilationRepository: CompilationRepository,
)  {

    suspend operator fun invoke(): Flow<PagingData<Compilation>>{

        val loggedUser = getUserFromStoreUseCase()

        Log.i(COMPILATION_MODEL, "Getting paginated compilations for user...")
        return compilationRepository.getPersonalCompilations(loggedUser!!.id)
    }
}