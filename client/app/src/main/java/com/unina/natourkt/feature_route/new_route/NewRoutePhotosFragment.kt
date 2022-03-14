package com.unina.natourkt.feature_route.new_route

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.util.setBottomMargin
import com.unina.natourkt.core.util.setTopMargin
import com.unina.natourkt.databinding.FragmentNewRoutePhotosBinding
import com.unina.natourkt.core.presentation.adapter.PhotoAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment

class NewRoutePhotosFragment : BaseFragment<FragmentNewRoutePhotosBinding, NewRouteViewModel>(),
    PhotoAdapter.OnItemClickListener {

    private val recyclerAdapter = PhotoAdapter(this@NewRoutePhotosFragment)

    private val viewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

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
                pickImageFromGallery(uiState.value.routePhotos) {
                    setPhotos(it)
                }
            }

            createRouteFab.setOnClickListener {
                this.uploadRoute()
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerNewPhotos.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@NewRoutePhotosFragment.requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                adapter = recyclerAdapter
            }
        }
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            recyclerAdapter.submitList(it.routePhotos)
        }

        collectLatestOnLifecycleScope(uiState) {
            binding.insertPhotoButton.isEnabled = it.routePhotos.size < 5
            binding.createRouteFab.isEnabled = it.routePhotos.isNotEmpty()
        }

        collectOnLifecycleScope(uiState) {
            if (it.isInserted) {
                findNavController().navigate(R.id.action_global_navigation_routes)
            }

            it.errorMessage?.run {
                manageMessage(this)
            }

            binding.progressBar.isVisible = it.isLoading
        }
    }


    override fun onRemoveClick(position: Int) {
        viewModel.removePhoto(position)
    }
}