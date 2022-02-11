package com.unina.natourkt.domain.usecase.datastore

import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.DataStoreRepository
import javax.inject.Inject

class GetUserFromStoreUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend operator fun invoke(): User? {
        return dataStoreRepository.getUserFromDataStore()
    }
}