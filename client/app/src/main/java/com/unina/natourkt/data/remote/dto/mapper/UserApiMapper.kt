package com.unina.natourkt.data.remote.dto.mapper

import com.unina.natourkt.data.remote.dto.UserDto
import com.unina.natourkt.domain.model.User
import javax.inject.Inject

class UserApiMapper @Inject constructor() : ApiMapper<UserDto, User> {

    override fun mapToDomain(apiEntity: UserDto): User {
        return User(
            id = apiEntity.id,
            username = apiEntity.username,
            profilePhoto = apiEntity.profilePhoto
        )
    }

    fun mapToDto(domainEntity: User): UserDto {
        return UserDto(
            id = domainEntity.id,
            username = domainEntity.username,
            profilePhoto = domainEntity.profilePhoto
        )
    }
}