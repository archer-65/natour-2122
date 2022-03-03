package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentNewRoutePhotosBinding
import com.unina.natourkt.presentation.base.adapter.NewPhotoAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment

class NewRoutePhotosFragment : BaseFragment<FragmentNewRoutePhotosBinding, NewRouteViewModel>(),
    NewPhotoAdapter.OnItemClickListener {

    private val recyclerAdapter = NewPhotoAdapter(this@NewRoutePhotosFragment)

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
        }
    }


    override fun onRemoveClick(position: Int) {
        viewModel.removePhoto(position)
    }
}