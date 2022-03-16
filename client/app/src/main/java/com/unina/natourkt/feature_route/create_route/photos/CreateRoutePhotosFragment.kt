package com.unina.natourkt.feature_route.create_route.photos

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.PhotoAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentCreateRoutePhotosBinding
import com.unina.natourkt.feature_route.create_route.CreateRouteEvent
import com.unina.natourkt.feature_route.create_route.CreateRouteViewModel

class CreateRoutePhotosFragment :
    BaseFragment<FragmentCreateRoutePhotosBinding, CreateRouteViewModel>(),
    PhotoAdapter.OnItemClickListener {

    private val recyclerAdapter = PhotoAdapter(this@CreateRoutePhotosFragment)

    private val viewModel: CreateRouteViewModel by hiltNavGraphViewModels(R.id.navigation_create_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentCreateRoutePhotosBinding.inflate(layoutInflater)

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
                pickImagesFromGallery(uiStatePhotos.value.photos) {
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

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiStatePhotos) {
                recyclerAdapter.submitList(it.photos)

                createRouteFab.isEnabled = it.isButtonEnabled
            }

            collectOnLifecycleScope(uiState) {
                if (it.isInserted) {
                    findNavController().navigate(R.id.action_global_navigation_routes)
                }

                progressBar.isVisible = it.isLoading
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            createRouteFab,
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).setAnchorView(createRouteFab).show()
                    }
                    else -> {}
                }
            }
        }
    }


    override fun onRemoveClick(position: Int) {
        viewModel.onEvent(CreateRouteEvent.RemovePhoto(position))
    }
}