package com.unina.natourkt.core.domain.model

data class Compilation(
    val id: Long,
    val title: String,
    val description: String,
    val photo: String,
    val author: User
)
