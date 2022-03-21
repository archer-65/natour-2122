package com.unina.natourkt.feature_post.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.contract.PlacesContract
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.databinding.FragmentBottomSheetFilterBinding
import com.unina.natourkt.databinding.FragmentBottomSheetPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.ClassCastException

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

        privateMsgButton.setOnClickListener {
        }
    }
}