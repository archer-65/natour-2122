package com.unina.natourkt.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.common.Constants.COLUMN_COUNT
import com.unina.natourkt.domain.model.route.RouteStop
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.main.MainActivity
import dev.chrisbanes.insetter.applyInsetter
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.TedImagePickerBaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

/**
 *  A function that makes the view visible.
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 *  A function that makes the view invisible.
 */
fun View.inVisible() {
    this.visibility = View.GONE
}

fun View.setTopMargin() {
    this.applyInsetter {
        type(statusBars = true) {
            margin()
        }
    }
}

fun View.setBottomMargin() {
    this.applyInsetter {
        type(navigationBars = true) {
            margin()
        }
    }
}

fun View.navigateOnClick(@IdRes resId: Int) {
    this.setOnClickListener {
        findNavController().navigate(resId)
    }
}

fun TextInputLayout.updateText(execute: (text: String) -> Unit) {
    this.editText?.doAfterTextChanged {
        val text = it.toString().trim()
        execute(text)
    }
}

fun ImageView.loadWithGlide(url: String?, @DrawableRes fallbackDrawable: Int) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(fallbackDrawable)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadWithGlide(url: String?) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun RecyclerView.scrollBehavior(fab: FloatingActionButton) {
    this.setOnScrollChangeListener { _, scrollX, scrollY, _, oldScrollY ->
        if (scrollY > oldScrollY) {
            fab.hide()
        } else if (scrollX == scrollY) {
            fab.show()
        } else {
            fab.show()
        }
    }
}

fun GoogleMap.addCustomMarker(title: String, latLng: LatLng, isDraggable: Boolean = false) {
    this.addMarker(
        MarkerOptions()
            .position(latLng)
            .title(title)
            .draggable(isDraggable)
    )
}

fun GoogleMap.moveAndZoomCamera(latLng: LatLng, zoom: Float = 15F) {
    this.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
}

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


fun String.toDateTime(): LocalDateTime =
    LocalDateTime.parse(this)

suspend fun Uri.toInputStream() = withContext(Dispatchers.Default) {
    runCatching {
        MainActivity.instance.contentResolver.openInputStream(this@toInputStream)
    }
}