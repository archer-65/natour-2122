package com.unina.natourkt.feature_route.report_route

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFullDialogFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.DialogReportRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportRouteFullDialog :
    BaseFullDialogFragment<DialogReportRouteBinding, ReportRouteViewModel>() {

    private val viewModel: ReportRouteViewModel by viewModels()

    override fun getViewBinding() = DialogReportRouteBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.toolbar.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        toolbar.apply {
            setNavigationOnClickListener {
                showHelperDialog(
                    title = R.string.cancel_report_dialog,
                    message = R.string.cancel_insertion_message,
                    icon = R.drawable.ic_warning_generic_24,
                    positive = R.string.yes_action_dialog,
                    negative = R.string.no_action_dialog
                ) {
                    dismiss()
                }
            }
        }

        sendReportButton.setOnClickListener {
            viewModel.onEvent(ReportRouteEvent.Upload)
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        reportTitleTextField.updateText {
            viewModel.onEvent(ReportRouteEvent.EnteredTitle(it))
        }

        descriptionTextField.updateText {
            viewModel.onEvent(ReportRouteEvent.EnteredDescription(it))
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                if (it.isInserted) {
                    dismiss()
                }

                sendReportButton.isEnabled = it.isButtonEnabled
                progressBar.isVisible = it.isLoading
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
                    is UiEvent.ShowToast -> {
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