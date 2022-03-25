package com.unina.natourkt.feature_post.delete_post

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseDialogFragment
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.databinding.DialogDeletePostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeletePostDialog : BaseDialogFragment<DialogDeletePostBinding, DeletePostViewModel>() {

    private val viewModel: DeletePostViewModel by viewModels()

    override fun getViewBinding() = DialogDeletePostBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override var baseTitle = R.string.delete_post_dialog_title
    override fun getDialogTitle() = baseTitle

    override var baseMessage = R.string.delete_post_dialog_message
    override fun getDialogMessage() = baseMessage

    override fun getDialogIcon() = R.drawable.ic_baseline_error_outline_24
    override fun getDialogPositive() = R.string.remove_dialog
    override fun getDialogNegative() = R.string.cancel_dialog

    override fun positiveAction() {
        viewModel.onEvent(DeletePostEvent.OnDelete)
    }

    override fun negativeAction() {
        findNavController().navigateUp()
    }

    override fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            if (it.isDeleted) {
                findNavController().navigate(R.id.action_deletePostDialog_to_navigation_home)
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