package com.unina.natourkt.presentation.new_post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentNewPostBinding
import com.unina.natourkt.presentation.base.adapter.PhotoAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment

class NewPostFragment : BaseFragment<FragmentNewPostBinding, NewPostViewModel>(),
    PhotoAdapter.OnItemClickListener {

    private val recyclerAdapter = PhotoAdapter(this@NewPostFragment)

    private val viewModel: NewPostViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentNewPostBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        postFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            insertPhotoButton.setOnClickListener {
                pickImageFromGallery(uiState.value.postPhotos) {
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
                        this@NewPostFragment.requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                adapter = recyclerAdapter
            }
        }
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            recyclerAdapter.submitList(it.postPhotos)
        }

        collectLatestOnLifecycleScope(uiState) {
            binding.insertPhotoButton.isEnabled = it.postPhotos.size < 5
            binding.postFab.isEnabled = it.postPhotos.isNotEmpty()
        }

        collectOnLifecycleScope(uiState) {
            if (it.isInserted) {
                findNavController().navigate(R.id.action_global_navigation_routes)
            }

            it.errorMessage?.run {
                manageMessage(this)
            }

            //binding.progressBar.isVisible = it.isLoading
        }
    }

    override fun onRemoveClick(position: Int) {
        viewModel.removePhoto(position)
    }
}