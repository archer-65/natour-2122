package com.unina.natourkt.core.domain.use_case.user

import android.net.Uri
import androidx.core.net.toUri
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.repository.UserRepository
import com.unina.natourkt.core.domain.use_case.storage.UploadFilesUseCase
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserPhotoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val uploadFilesUseCase: UploadFilesUseCase,
) {

    operator fun invoke(user: User): Flow<DataState<User>> = flow {
        emit(DataState.Loading())

        val userWithUploadedPhoto = uploadPhoto(user)
        val result = userRepository.updateUser(userWithUploadedPhoto)

        emit(result)
    }

    private suspend fun uploadPhoto(user: User): User {
        val pathDefinition = definePath(user)
        val uploadedPhoto =
            uploadFilesUseCase(pathDefinition, user.profilePhoto?.toUri() ?: Uri.EMPTY)

        return user.copy(profilePhoto = uploadedPhoto)
    }

    private fun definePath(user: User): String {
        val prefix = "users/${user.id}"
        val path = "${prefix}/profilePhoto"
        return path
    }
}