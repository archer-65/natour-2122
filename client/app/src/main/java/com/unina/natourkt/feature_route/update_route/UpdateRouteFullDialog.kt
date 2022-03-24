package com.unina.natourkt.feature_route.update_route

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFullDialogFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.DialogUpdateRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateRouteFullDialog :
    BaseFullDialogFragment<DialogUpdateRouteBinding, UpdateRouteViewModel>() {

    private val viewModel: UpdateRouteViewModel by viewModels()

    override fun getViewBinding() = DialogUpdateRouteBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.toolbar.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            showHelperDialog(
                title = R.string.cancel_update_route_dialog,
                message = R.string.cancel_insertion_message,
                icon = R.drawable.ic_warning_generic_24,
                positive = R.string.yes_action_dialog,
                negative = R.string.no_action_dialog
            ) {
                dismiss()
            }
        }

        updateRouteButton.setOnClickListener {
            viewModel.onEvent(UpdateRouteEvent.Upload)
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            descriptionTextField.updateText {
                onEvent(UpdateRouteEvent.EnteredDescription(it))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            descriptionTextField.editText?.setText(uiState.value.newDescription.text)

            collectLatestOnLifecycleScope(uiState) {
                if (it.isUpdated) {
                    dismiss()
                }

                updateRouteButton.isEnabled = it.isButtonEnabled
                progressBar.isVisible = it.isLoading
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEffect.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is UiEffect.ShowToast -> {
                        Toast.makeText(
                            requireContext(),
                            event.uiText.asString(requireContext()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }
}