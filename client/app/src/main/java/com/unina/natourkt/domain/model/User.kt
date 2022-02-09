package com.unina.natourkt.domain.model

data class User(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val photo: String
)
