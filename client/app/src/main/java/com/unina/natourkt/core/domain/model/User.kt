package com.unina.natourkt.core.domain.model

/**
 * User domain model (to improve)
 */
data class User(
    val id: Long,
    val username: String,
    val isAdmin: Boolean = false,
    val profilePhoto: String?
) {

    suspend fun convertKeys(execute: suspend (string: String) -> String): User {
        val newPhoto = this.profilePhoto?.let { execute(it) }
        return this.copy(profilePhoto = newPhoto)
    }
}