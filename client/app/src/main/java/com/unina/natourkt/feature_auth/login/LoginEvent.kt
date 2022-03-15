package com.unina.natourkt.feature_auth.login

sealed class LoginEvent {
    data class EnteredUsername(val username: String) : LoginEvent()
    data class EnteredPassword(val password: String) : LoginEvent()
    data class LoginSocial(val provider: String) : LoginEvent()
    object Login : LoginEvent()
}