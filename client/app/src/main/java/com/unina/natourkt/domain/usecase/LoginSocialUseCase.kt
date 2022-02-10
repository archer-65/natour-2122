package com.unina.natourkt.domain.usecase

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.fragment.app.FragmentActivity
import com.amplifyframework.auth.AuthException
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * This UseCase make use of
 * - [AuthRepository] to login the user through socials
 * - [DataStoreRepository] to persist the user on DataStore Preferences
 * - [UserRepository] to retrieve the user through REST Service
 */
class LoginSocialUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
) {

    operator fun invoke(provider: String): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading())

            val isSignInComplete = authRepository.login(provider)

            if (isSignInComplete) {
                // If user's login is successful, save the user locally in DataStore Preferences
                val cognitoId = authRepository.fetchUserSub()
                val user = userRepository.getUserByCognitoId(cognitoId).toUser()
                dataStoreRepository.saveUserToDataStore(user);

                emit(DataState.Success(isSignInComplete))
            } else {
                emit(DataState.Error(context.getString(R.string.auth_failed_generic)))
            }
        } catch (e: AuthException) {
            emit(DataState.Error(context.getString(R.string.auth_failed_exception)))
        } catch (e: CorruptionException) {
            emit(DataState.Error(context.getString(R.string.data_corrupted)))
        } catch (e: HttpException) {
            emit(DataState.Error(context.getString(R.string.retrofit_http_error)))
        } catch (e: IOException) {
            emit(DataState.Error(context.getString(R.string.internet_error)))
        }
    }
}