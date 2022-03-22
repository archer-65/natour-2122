package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.presentation.model.UserUi
import javax.inject.Inject

class UserUiMapper @Inject constructor() : UiMapper<User, UserUi> {

    override fun mapToUi(domainEntity: User): UserUi {
        return UserUi(
            id = domainEntity.id,
            username = domainEntity.username,
            isAdmin = domainEntity.isAdmin,
            photo = domainEntity.profilePhoto
        )
    }

    fun mapToDomain(uiEntity: UserUi): User {
        return User(
            id = uiEntity.id,
            username = uiEntity.username,
            isAdmin = uiEntity.isAdmin,
            profilePhoto = uiEntity.photo
        )
    }
}