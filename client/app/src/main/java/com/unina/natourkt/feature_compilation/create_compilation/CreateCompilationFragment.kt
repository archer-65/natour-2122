package com.unina.natourkt.feature_compilation.create_compilation

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.FragmentCreateCompilationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCompilationFragment :
    BaseFragment<FragmentCreateCompilationBinding, CreateCompilationViewModel>() {

    private val viewModel: CreateCompilationViewModel by viewModels()

    override fun getViewBinding() = FragmentCreateCompilationBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        createCompilationFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            createCompilationFab.setOnClickListener {
                onEvent(CreateCompilationEvent.Upload)
            }

            insertPhotoButton.setOnClickListener {
                pickImageFromGallery() {
                    onEvent(CreateCompilationEvent.InsertedPhoto(it))
                }
            }

            topAppBar.setNavigationOnClickListener {
                showHelperDialog(
                    title = R.string.cancel_insertion,
                    message = R.string.cancel_insertion_message,
                    icon = R.drawable.ic_warning_generic_24,
                    positive = R.string.yes_action_dialog,
                    negative = R.string.no_action_dialog
                ) {
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            compilationTitleTextField.updateText {
                onEvent(CreateCompilationEvent.EnteredTitle(it))
            }

            descriptionTextField.updateText {
                onEvent(CreateCompilationEvent.EnteredDescription(it))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                if (it.inInserted) {
                    findNavController().navigate(R.id.action_create_compilation_to_profile)
                }

                it.compilationPhoto.apply {
                    if (this != Uri.EMPTY) {
                        compilationNewPhoto.setImageURI(this)
                        compilationNewPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                }

                createCompilationFab.isEnabled = it.isButtonEnabled
                progressBar.isVisible = it.isLoading
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEffect.ShowSnackbar -> {
                        Snackbar.make(
                            createCompilationFab,
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).setAnchorView(createCompilationFab).show()
                    }
                    else -> {}
                }
            }
        }
    }
}