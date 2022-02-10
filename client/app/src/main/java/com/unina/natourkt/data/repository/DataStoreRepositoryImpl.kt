package com.unina.natourkt.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.unina.natourkt.common.Constants.PREFERENCES
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
        var user: User? = null

        context.dataStore.data.map {
            User(
                id = it[ID]!!,
                username = it[USERNAME]!!,
                isAdmin = it[IS_ADMIN] ?: false,
                photo = it[PHOTO]
            )
        }.onEach { userStored ->
            user = userStored
        }

        return user
    }
}