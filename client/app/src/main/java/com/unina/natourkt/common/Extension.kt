package com.unina.natourkt.common

import android.view.View
import com.google.android.material.textfield.TextInputLayout

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.GONE
}

fun TextInputLayout.newError(message: String?) {
    this.error = message
}