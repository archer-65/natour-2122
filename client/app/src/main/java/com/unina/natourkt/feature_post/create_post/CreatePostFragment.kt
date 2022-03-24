package com.unina.natourkt.feature_post.create_post

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.PhotoAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.FragmentCreatePostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : BaseFragment<FragmentCreatePostBinding, CreatePostViewModel>(),
    PhotoAdapter.OnItemClickListener {

    private val recyclerAdapter = PhotoAdapter(this@CreatePostFragment)

    private val viewModel: CreatePostViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentCreatePostBinding.inflate(layoutInflater)

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        postFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            insertPhotoButton.setOnClickListener {
                pickImagesFromGallery(uiState.value.postPhotos) {
                    onEvent(CreatePostEvent.InsertedPhotos(it))
                }
            }

            postFab.setOnClickListener {
                onEvent(CreatePostEvent.Upload)
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerNewPhotos.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@CreatePostFragment.requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        true,
                    )
                adapter = recyclerAdapter
            }
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            descriptionTextField.updateText {
                onEvent(CreatePostEvent.EnteredDescription(it))
            }

            routeTextField.updateText {
                onEvent(CreatePostEvent.EnteredQuery(it))
                onEvent(CreatePostEvent.EnteredRoute(it))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                if (it.isInserted) {
                    findNavController().navigate(R.id.action_create_post_to_home)
                }

                recyclerAdapter.submitList(it.postPhotos)

                postFab.isEnabled = it.isButtonEnabled
                progressBar.isVisible = it.isLoading
            }

            collectOnLifecycleScope(upcomingRoutes) {
                val updated = it.map { it.routeTitle }.toTypedArray()
                (routeTextField.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(updated)
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            postFab,
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).setAnchorView(postFab).show()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onRemoveClick(position: Int) {
        viewModel.onEvent(CreatePostEvent.RemovePhoto(position))
    }
}