package com.unina.natourkt.core.presentation.base.validation

import android.util.Patterns
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.Constants.CODE_LENGTH

//fun String.isPasswordValid(): Boolean {
//    return this.length >= Constants.PASSWORD_LENGTH
//}
//
//fun String.isUsernameValid(): Boolean {
//    return !this.contains(" ")
//}
//
//fun String.isEmailValid(): Boolean {
//    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
//}
//
//fun String.isCodeValid(): Boolean {
//    return this.length == CODE_LENGTH
//}

fun String.isUsernameValid(): UiText.StringResource? {
    return if (!this.contains(" ")) {
        null
    } else {
        UiText.StringResource(R.string.username_check)
    }
}

fun String.isEmailValid(): UiText.StringResource? {
    return if (Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        null
    } else {
        UiText.StringResource(R.string.email_check)
    }
}

fun String.isPasswordValid(): UiText.StringResource? {
    return if (this.length >= Constants.PASSWORD_LENGTH) {
        null
    } else {
        UiText.StringResource(R.string.password_length)
    }
}


fun String.isCodeValid(): UiText.StringResource? {
    return if (this.length == CODE_LENGTH) {
        null
    } else {
        UiText.StringResource(R.string.code_check)
    }
}

fun String.equalsOtherString(other: String): UiText.StringResource? {
    return if (this.equals(other)) {
        null
    } else {
        UiText.StringResource(R.string.passwords_matching)
    }
}