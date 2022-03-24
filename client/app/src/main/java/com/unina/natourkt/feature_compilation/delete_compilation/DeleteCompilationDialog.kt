package com.unina.natourkt.feature_compilation.delete_compilation

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseDialogFragment
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.databinding.DialogDeleteCompilationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteCompilationDialog :
    BaseDialogFragment<DialogDeleteCompilationBinding, DeleteCompilationViewModel>() {

    private val viewModel: DeleteCompilationViewModel by viewModels()

    override fun getViewBinding() = DialogDeleteCompilationBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override var baseTitle: Int = R.string.delete_compilation_title_dialog
    override fun getDialogTitle() = baseTitle

    override var baseMessage: Int = R.string.delete_compilation_message_dialog
    override fun getDialogMessage() = baseMessage

    override fun getDialogIcon() = R.drawable.ic_outline_delete_24
    override fun getDialogPositive() = R.string.remove_dialog
    override fun getDialogNegative() = R.string.cancel_dialog

    override fun positiveAction() {
        viewModel.onEvent(DeleteCompilationEvent.OnDelete)
    }

    override fun negativeAction() {
        dismiss()
    }

    override fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            if (it.isDeleted) {
                findNavController().navigate(R.id.action_deleteCompilationDialog_to_navigation_profile)
            }
        }

        collectLatestOnLifecycleScope(viewModel.eventFlow) { event ->
            when (event) {
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