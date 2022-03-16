package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.CompilationCreationDto
import com.unina.natourkt.core.domain.model.CompilationCreation
import javax.inject.Inject

class CompilationCreationApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper
) :
    CreationApiMapper<CompilationCreation, CompilationCreationDto> {

    override fun mapToDto(domainEntity: CompilationCreation): CompilationCreationDto {
        return CompilationCreationDto(
            title = domainEntity.title,
            description = domainEntity.description,
            photo = domainEntity.photo,
            author = userApiMapper.mapToDto(domainEntity.author!!)
        )
    }
}