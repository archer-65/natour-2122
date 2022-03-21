package com.unina.natourkt.core.presentation.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.core.util.Constants
import dev.chrisbanes.insetter.applyInsetter
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

fun RecyclerView.scrollChat(fab: FloatingActionButton) {
    this.setOnScrollChangeListener { _, scrollX, scrollY, _, oldScrollY ->
        if (!this.canScrollVertically(1)) {
            fab.hide()
        } else {
            fab.show()
        }
    }
}

/**
 * This function serves as a fast way to open an Image Picker.
 * [TedImagePicker] is a really nice library, the default `Picker` is a bit limited
 * and at the moment `Photo Picker` is available only for Preview SDK `Tiramisu`.
 */
fun Fragment.pickImagesFromGallery(
    selected: List<Uri>,
    execute: (images: List<Uri>) -> Unit
) {
    val context = requireContext()

    TedImagePicker
        .with(context)
        .title(context.getString(R.string.select_images))
        .mediaType(MediaType.IMAGE)
        .selectedUri(selected)
        .max(Constants.MAX_PHOTO, context.getString(R.string.no_more_photo))
        .buttonText(context.getString(R.string.done_select_photos))
        .buttonBackground(R.color.md_theme_light_background)
        .buttonTextColor(R.color.md_theme_light_primary)
        .startMultiImage { uriList ->
            execute(uriList)
        }
}

fun Fragment.pickImageFromGallery(execute: (image: Uri) -> Unit) {
    val context = requireContext()

    TedImagePicker
        .with(context)
        .title(context.getString(R.string.select_single_image))
        .mediaType(MediaType.IMAGE)
        .start { uri ->
            execute(uri)
        }
}

/**
 * It launches a coroutine on the view's lifecycle scope, and repeats the coroutine on the view's
 * lifecycle until the view's lifecycle is in the STARTED state
 */
fun <T> Fragment.collectLatestOnLifecycleScope(flow: Flow<T>, execute: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(execute)
        }
    }
}

/**
 * It launches a coroutine on the view's lifecycle scope, and repeats the coroutine on the view's
 * lifecycle until the view's lifecycle is in the STARTED state
 */
fun <T> Fragment.collectOnLifecycleScope(flow: Flow<T>, execute: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect() {
                execute(it)
            }
        }
    }
}

fun Fragment.showHelperDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @DrawableRes icon: Int,
    @StringRes positive: Int,
    @StringRes negative: Int? = null,
    execute: (() -> Unit)? = null,
) {
    val allDialog = MaterialAlertDialogBuilder(requireContext())
        .setIcon(icon)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positive) { dialog, _ ->
            execute?.let { it() } ?: dialog.dismiss()
        }

    val completeDialog = negative?.let {
        allDialog.setNegativeButton(it) { dialog, _ ->
            dialog.dismiss()
        }
    }

    completeDialog?.show() ?: allDialog.show()
}