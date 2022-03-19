package com.unina.natourkt.core.presentation.base.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.util.Constants.MAX_PHOTO
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.presentation.main.MainViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This open class extending [Fragment] provides basic functionality
 * for Fragments which extends it
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    /* A way to get the MainViewModel from the activity, and not from the fragment. */
    /* val mainViewModel: MainViewModel by activityViewModels() */

    /**
     * This property serves as ViewModel generalization
     */
    protected lateinit var baseViewModel: VM
    protected abstract fun getVM(): VM

    /**
     * This property is valid only until onDestroyView is called
     */
    protected var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract fun getViewBinding(): VB


    /**
     * Overrides `onCreateView` only to return binding's root view, initialized in [init]
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    /**
     * Overrides `onViewCreated`, here all the useful basic functions are called
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        collectState()
    }

    /**
     * [_binding] is set to null. This because Fragments could outlive their views, so we clean up
     * every reference to the [ViewBinding] class
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * It instantiate a binding object for the view, and initializes the view model
     */
    open fun init() {
        _binding = getViewBinding()
        baseViewModel = getVM()
    }

    /**
     * This function setups every View
     */
    open fun setupUi() {}

    /**
     * This function sets any kind of listener
     */
    open fun setListeners() {}

    /**
     * This function sets any kind of text listener
     */
    open fun setTextChangedListeners() {}

    /**
     * This function initialize any recycler view
     */
    open fun initRecycler() {}

    /**
     * This function initialize a [ConcatAdapter]
     */
    open fun initConcatAdapter(): ConcatAdapter {
        return ConcatAdapter()
    }

    /**
     * This function serves as a way to collect states from ViewModel
     */
    open fun collectState() {}

    /**
     * It launches a coroutine on the view's lifecycle scope, and repeats the coroutine on the view's
     * lifecycle until the view's lifecycle is in the STARTED state
     */
    fun <T> collectLatestOnLifecycleScope(flow: Flow<T>, execute: suspend (T) -> Unit) {
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
    fun <T> collectOnLifecycleScope(flow: Flow<T>, execute: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect() {
                    execute(it)
                }
            }
        }
    }

    /**
     * This function serves as a fast way to open an Image Picker.
     * [TedImagePicker] is a really nice library, the default `Picker` is a bit limited
     * and at the moment `Photo Picker` is available only for Preview SDK `Tiramisu`.
     */
    fun pickImagesFromGallery(selected: List<Uri>, execute: (images: List<Uri>) -> Unit) {
        TedImagePicker
            .with(requireContext())
            .title(getString(R.string.select_images))
            .mediaType(MediaType.IMAGE)
            .selectedUri(selected)
            .max(MAX_PHOTO, getString(R.string.no_more_photo))
            .buttonText(getString(R.string.done_select_photos))
            .buttonBackground(R.color.md_theme_light_background)
            .buttonTextColor(R.color.md_theme_light_primary)
            .startMultiImage { uriList ->
                execute(uriList)
            }
    }

    fun pickImageFromGallery(execute: (image: Uri) -> Unit) {
        TedImagePicker
            .with(requireContext())
            .title("Seleziona immagine")
            .mediaType(MediaType.IMAGE)
            .start { uri ->
                execute(uri)
            }
    }
}