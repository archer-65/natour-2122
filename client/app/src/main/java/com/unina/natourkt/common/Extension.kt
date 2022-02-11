package com.unina.natourkt.common

import android.view.View

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.inVisible(){
    this.visibility = View.GONE
}