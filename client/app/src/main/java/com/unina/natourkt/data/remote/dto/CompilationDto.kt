package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.Compilation
import java.time.LocalDateTime

data class CompilationDto(
    val id: Long,
    val title: String,
    val description: String,
    val creationDate: String,
    val photo: String,
    val user: UserDto
)

fun CompilationDto.toCompilation(): Compilation{
    return Compilation(
        id = id,
        title = title,
        description = description,
        photo = photo,
        user = user.toUser()
    )
}
