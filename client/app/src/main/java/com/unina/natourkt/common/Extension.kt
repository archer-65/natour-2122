package com.unina.natourkt.common

import android.view.View
import androidx.annotation.IntegerRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

// General
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.GONE
}

// Snackbar
fun View.showSnackBar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    action: (Snackbar.() -> Unit)? = null
) {

    val snackbar = Snackbar.make(this, message, length)
    action?.let { snackbar.it() }
    snackbar.show()
}