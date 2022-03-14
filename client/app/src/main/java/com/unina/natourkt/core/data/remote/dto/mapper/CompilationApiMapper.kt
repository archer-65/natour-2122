package com.unina.natourkt.core.data.remote.dto.mapper

import com.unina.natourkt.core.data.remote.dto.CompilationDto
import com.unina.natourkt.core.domain.model.Compilation
import javax.inject.Inject

class CompilationApiMapper @Inject constructor(
    private val userApiMapper: UserApiMapper
) : ApiMapper<CompilationDto, Compilation> {

    override fun mapToDomain(apiEntity: CompilationDto): Compilation {
        return Compilation(
            id = apiEntity.id,
            title = apiEntity.title,
            description = apiEntity.description,
            photo = apiEntity.photo,
            author = userApiMapper.mapToDomain(apiEntity.author)
        )
    }
}