package com.unina.natourkt.data.remote.dto

import com.unina.natourkt.domain.model.Compilation
import java.time.LocalDateTime

data class CompilationDto(
    val id: Long,
    val title: String,
    val description: String,
    val creationDate: String,
    val photoUrl: String,
    val user: UserDto
)

fun CompilationDto.toCompilation(): Compilation{
    return Compilation(
        id = id,
        title = title,
        description = description,
        photoUrl = photoUrl,
        user = user.toUser()
    )
}
