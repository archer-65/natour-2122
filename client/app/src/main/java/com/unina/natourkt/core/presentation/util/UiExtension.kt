package com.unina.natourkt.core.presentation.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dev.chrisbanes.insetter.applyInsetter

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

fun ImageSlider.load(photos: List<String>) {
    val imageList = photos.map { SlideModel(it) }
    this.setImageList(imageList, ScaleTypes.CENTER_CROP)
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

fun RecyclerView.scrollBehavior(extendedFab: ExtendedFloatingActionButton) {
    this.setOnScrollChangeListener { _, scrollX, scrollY, _, oldScrollY ->
        if (scrollY > oldScrollY) {
            extendedFab.shrink()
        } else if (scrollX == scrollY) {
            extendedFab.extend()
        } else {
            extendedFab.extend()
        }
    }
}