package com.unina.natourkt.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompilationCreationDto(
    @SerializedName("compilation_title")
    val title: String,

    @SerializedName("compilation_description")
    val description: String,

    @SerializedName("compilation_photo")
    val photo: String,

    @SerializedName("compilation_author")
    val author: UserDto
)