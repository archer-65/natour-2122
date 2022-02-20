package com.unina.natourkt.common

import android.view.View
import androidx.annotation.IntegerRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

/**
 * Makes [View] VISIBLE
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Makes [View] GONE
 */
fun View.inVisible() {
    this.visibility = View.GONE
}