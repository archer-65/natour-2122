package com.unina.natourkt.domain.repository

import com.unina.natourkt.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveUserToDataStore(user: User)

    suspend fun getUserFromDataStore(): User

//    suspend fun putString(key: String, value: String)
//
//    suspend fun putLong(key: String, value: Long)
//
//    suspend fun putBoolean(key: String, value: Boolean)
//
//    suspend fun getString(key: String): String?
//
//    suspend fun getLong(key: String): Long?
//
//    suspend fun getBoolean(key: String): Boolean?
}