package com.unina.natourkt.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.unina.natourkt.common.Constants.PREFERENCES
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DataStoreRepository {

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = PREFERENCES)

    /**
     * Companion object to simulate an object User in preferences
     */
    companion object {

        val ID = longPreferencesKey("USER_ID")
        val USERNAME = stringPreferencesKey("USERNAME")
        val IS_ADMIN = booleanPreferencesKey("IS_ADMIN")
        val PHOTO = stringPreferencesKey("PHOTO")

    }

    /**
     * Only edit the datastore with given user
     */
    override suspend fun saveUserToDataStore(user: User) {
        Log.i("DATASTORE", "$user")
        context.dataStore.edit {
            it[ID] = user.id
            it[USERNAME] = user.username
            it[IS_ADMIN] = user.isAdmin
            it[PHOTO] = user.photo ?: ""
        }
    }

    /**
     * Retrieve the logged user
     */
    override suspend fun getUserFromDataStore(): User? {
        return try {
            context.dataStore.data.map {
                User(
                    id = it[ID]!!,
                    username = it[USERNAME]!!,
                    isAdmin = it[IS_ADMIN] ?: false,
                    photo = it[PHOTO]
                )
            }.first()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}