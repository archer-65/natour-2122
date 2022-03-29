package com.unina.natourkt.core.data.fake

import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.DataState

class FakeAuthRepository : AuthRepository {

    val registeredUsers = mutableListOf(
        RegisteredUser("BiancaChehade0705", "biancachehade@hotmail.com", "ingswinteressante"),
        RegisteredUser("mariomartin", "martinrouterking@live.it", "mugiwara"),
        RegisteredUser("affredogiovanni", "camillacabello@outlook.com", "testingsignup")
    )

    override suspend fun fetchCurrentSession(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): DataState<Boolean> {
        if (registeredUsers.any { it.username == username }) {
            return DataState.Error(DataState.Cause.AliasExists)
        } else {
            registeredUsers.add(RegisteredUser(username, email, password))
            return DataState.Success(true)
        }
    }

    override suspend fun confirmRegistration(username: String, code: String): DataState<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun login(username: String, password: String): DataState<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun login(provider: String): DataState<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun resendCode(username: String): DataState<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPasswordRequest(username: String): DataState<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPasswordConfirm(password: String, code: String): DataState<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): DataState<Boolean> {
        TODO("Not yet implemented")
    }
}