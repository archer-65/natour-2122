package com.unina.natourkt.presentation.base.validation

import android.util.Patterns
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants

fun String.isPasswordValid(): Boolean {
    return this.length >= Constants.PASSWORD_LENGTH
}

fun String.isUsernameValid(): Boolean {
    return !this.contains(" ")
}

fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isCodeValid(): Boolean {
    return this.length == 6
}