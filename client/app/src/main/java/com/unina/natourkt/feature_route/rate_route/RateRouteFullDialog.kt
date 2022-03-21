package com.unina.natourkt.feature_route.rate_route

import android.text.InputFilter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFullDialogFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.databinding.DialogRateRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateRouteFullDialog : BaseFullDialogFragment<DialogRateRouteBinding, RateRouteViewModel>() {

    private val viewModel: RateRouteViewModel by viewModels()

    override fun getViewBinding() = DialogRateRouteBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.toolbar.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        toolbar.setNavigationOnClickListener {
            showHelperDialog(
                title = R.string.cancel_rate_dialog,
                message = R.string.cancel_insertion_message,
                icon = R.drawable.ic_warning_generic_24,
                positive = R.string.yes_action_dialog,
                negative = R.string.no_action_dialog
            ) {
                dismiss()
            }
        }

        durationTextField.editText?.apply {
            filters = arrayOf<InputFilter>(DurationFilter(1, 16))
        }

        sendRatingButton.setOnClickListener {
            viewModel.onEvent(RateRouteEvent.Upload)
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel)
        {
            durationTextField.updateText {
                onEvent(RateRouteEvent.EnteredDuration(it))
            }

            difficultyChipgroup.setOnCheckedStateChangeListener { group, _ ->
                val checkedDifficulty = when (group.checkedChipId) {
                    easyChip.id -> Difficulty.EASY
                    mediumChip.id -> Difficulty.MEDIUM
                    hardChip.id -> Difficulty.HARD
                    else -> Difficulty.EASY
                }
                onEvent(RateRouteEvent.EnteredDifficulty(checkedDifficulty))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                if (it.isInserted) {
                    dismiss()
                }

                sendRatingButton.isEnabled = it.isButtonEnabled
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