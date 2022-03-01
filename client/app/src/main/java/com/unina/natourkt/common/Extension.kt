package com.unina.natourkt.common

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unina.natourkt.domain.model.route.RouteStop

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

fun ImageView.loadWithGlide(url: String?, @DrawableRes fallbackDrawable: Int) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .fallback(fallbackDrawable)
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

fun String.decodePolyline(): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = this.length
    var lat = 0
    var lng = 0
    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = this[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat
        shift = 0
        result = 0
        do {
            b = this[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng
        val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
        poly.add(latLng)
    }
    return poly
}