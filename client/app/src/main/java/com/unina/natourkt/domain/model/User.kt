package com.unina.natourkt.domain.model

/**
 * User model (to improve)
 */
data class User(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val photo: String?
)
