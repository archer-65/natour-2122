package com.unina.natourkt.feature_post.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unina.natourkt.databinding.FragmentBottomSheetPostBinding

class BottomSheetHomeFragment : BottomSheetDialogFragment() {

    private val args: BottomSheetHomeFragmentArgs by navArgs()

    private var _binding: FragmentBottomSheetPostBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() = with(binding) {
        reportPostButton.setOnClickListener {
            val action =
                BottomSheetHomeFragmentDirections.actionBottomSheetHomeFragment2ToReportPostDialog(
                    args.postForBottomSheet.id
                )
            findNavController().navigate(action)
        }
    }
}