package com.unina.natourkt.domain.use_case.datastore

import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveUserToStoreUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
){

    suspend operator fun invoke(user: User) {
        dataStoreRepository.saveUserToDataStore(user)
    }
}