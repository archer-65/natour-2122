package com.unina.natourkt.feature_profile.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.pickImageFromGallery
import com.unina.natourkt.databinding.BottomSheetFilterBinding
import com.unina.natourkt.databinding.BottomSheetProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: BottomSheetProfileBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() = with(binding) {
        with(viewModel) {
            updateProfilePhotoButton.setOnClickListener {
                pickImageFromGallery {
                    onEvent(ProfileEvent.OnUpdatePhoto(it))
                }
            }

            removeProfilePhotoButton.setOnClickListener {
                onEvent(ProfileEvent.OnUpdatePhoto(Uri.EMPTY))
            }
        }
    }

    private fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            if (it.isPhotoUpdated) {
                dismiss()
            }
        }
    }
}