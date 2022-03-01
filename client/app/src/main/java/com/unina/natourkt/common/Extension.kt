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
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
