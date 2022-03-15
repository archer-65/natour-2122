package com.unina.natourkt.feature_route.create_route.photos

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRoutePhotosBinding
import com.unina.natourkt.core.presentation.adapter.PhotoAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.feature_route.create_route.CreateRouteEvent
import com.unina.natourkt.feature_route.create_route.CreateRouteViewModel

class CreateRoutePhotosFragment :
    BaseFragment<FragmentNewRoutePhotosBinding, CreateRouteViewModel>(),
    PhotoAdapter.OnItemClickListener {

    private val recyclerAdapter = PhotoAdapter(this@CreateRoutePhotosFragment)

    private val viewModel: CreateRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentNewRoutePhotosBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        createRouteFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            insertPhotoButton.setOnClickListener {
                pickImageFromGallery(uiStatePhotos.value.photos) {
                    onEvent(CreateRouteEvent.InsertedPhotos(it))
                }
            }

            createRouteFab.setOnClickListener {
                onEvent(CreateRouteEvent.Upload)
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerNewPhotos.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@CreateRoutePhotosFragment.requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                adapter = recyclerAdapter
            }
        }
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiStatePhotos) {
            recyclerAdapter.submitList(it.photos)

            binding.createRouteFab.isEnabled = it.isButtonEnabled
        }

        collectOnLifecycleScope(uiState) {
            if (it.isInserted) {
                findNavController().navigate(R.id.action_global_navigation_routes)
            }

            binding.progressBar.isVisible = it.isLoading
        }

        collectLatestOnLifecycleScope(eventFlow) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Snackbar.make(
                        requireView(),
                        event.uiText.asString(requireContext()),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }


    override fun onRemoveClick(position: Int) {
        viewModel.onEvent(CreateRouteEvent.RemovePhoto(position))
    }
}