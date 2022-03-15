package com.unina.natourkt.core.util

import android.net.Uri
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.core.presentation.main.MainActivity
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

fun <T> List<T>.safeRemove(position: Int): List<T> {
    val newList = this.toMutableList().apply {
        removeAt(position)
    }.toList()

    return newList
}

suspend fun String.convertKeyToUrl(execute: suspend (text: String) -> String): String {
    return if (!URLUtil.isValidUrl(this)) {
        execute(this)
    } else {
        this
    }
}

suspend fun Uri.toInputStream() = withContext(Dispatchers.Default) {
    runCatching {
        MainActivity.instance.contentResolver.openInputStream(this@toInputStream)
    }
}