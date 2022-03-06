package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.domain.model.Compilation
import java.time.LocalDateTime

data class CompilationDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("creationDate")
    val creationDate: String,

    @SerializedName("photo")
    val photo: String,

    @SerializedName("user")
    val author: UserDto
)

fun CompilationDto.toCompilation(): Compilation {
    return Compilation(
        id = id,
        title = title,
        description = description,
        photo = photo,
        user = author.toUser()
    )
}
