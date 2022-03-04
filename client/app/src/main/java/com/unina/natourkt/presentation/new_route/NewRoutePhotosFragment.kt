package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentNewRoutePhotosBinding
import com.unina.natourkt.presentation.base.adapter.PhotoAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment

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
        nextFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            insertPhotoButton.setOnClickListener {
                pickImageFromGallery(uiState.value.routePhotos) {
                    setPhotos(it)
                }
            }

            nextFab.setOnClickListener {
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
            binding.nextFab.isEnabled = it.routePhotos.isNotEmpty()
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