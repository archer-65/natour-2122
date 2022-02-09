package com.unina.natourkt.domain.repository

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)

    suspend fun putLong(key: String, value: Long)

    suspend fun putBoolean(key: String, value: Boolean)

    suspend fun getString(key: String): String?

    suspend fun getLong(key: String): Long?

    suspend fun getBoolean(key: String): Boolean?
}