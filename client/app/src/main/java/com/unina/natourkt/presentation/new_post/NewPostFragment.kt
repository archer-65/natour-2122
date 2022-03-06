package com.unina.natourkt.presentation.new_post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.unina.natourkt.R
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentNewPostBinding
import com.unina.natourkt.presentation.base.adapter.PhotoAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        setTextChangedListeners()
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

            postFab.setOnClickListener {
                uploadPost()
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

    override fun setTextChangedListeners() {
        super.setTextChangedListeners()

        binding.routeTextField.editText?.doAfterTextChanged {
            viewModel.updateRoutes(it.toString().trim())
            viewModel.setRoute(it.toString().trim())
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                recyclerAdapter.submitList(it.postPhotos)

                insertPhotoButton.isEnabled = it.postPhotos.size < 5
                postFab.isEnabled = it.postPhotos.isNotEmpty() && it.route != null

                if (it.isInserted) {
                    findNavController().navigate(R.id.action_newPostFragment_to_navigation_home)
                }

                it.errorMessage?.run {
                    manageMessage(this)
                }
                //progressBar.isVisible = it.isLoading
            }

            collectOnLifecycleScope(upcomingRoutes) { it ->
                val updated = it.routes.map { it.routeTitle }.toTypedArray()
                (routeTextField.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
                    updated
                )
            }
        }
    }

    override fun onRemoveClick(position: Int) {
        viewModel.removePhoto(position)
    }
}