package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

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