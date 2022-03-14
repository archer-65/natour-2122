package com.unina.natourkt.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.unina.natourkt.domain.model.Compilation
import java.time.LocalDateTime

data class CompilationDto(
    @SerializedName("compilation_id")
    val id: Long,

    @SerializedName("compilation_title")
    val title: String,

    @SerializedName("compilation_description")
    val description: String,

    @SerializedName("compilation_creation_date")
    val creationDate: String,

    @SerializedName("compilation_photo")
    val photo: String,

    @SerializedName("compilation_author")
    val author: UserDto
)