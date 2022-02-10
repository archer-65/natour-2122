package com.unina.natourkt.domain.repository

import com.unina.natourkt.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveUserToDataStore(user: User)

    suspend fun getUserFromDataStore(): User
}