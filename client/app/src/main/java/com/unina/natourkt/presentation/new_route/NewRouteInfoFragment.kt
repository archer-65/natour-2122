package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteInfoBinding
import com.unina.natourkt.databinding.FragmentRouteDetailsBinding
import com.unina.natourkt.presentation.base.input_filter.DurationFilter
import dev.chrisbanes.insetter.applyInsetter
import java.lang.NumberFormatException

class NewRouteInfoFragment : Fragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentNewRouteInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewRouteInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUi()

        return root
    }

    private fun setupUi() {
        binding.apply {
            topAppBar.apply {
                applyInsetter {
                    type(statusBars = true) {
                        margin()
                    }
                }
            }

            textfieldDuration.editText?.apply {
                filters = arrayOf<InputFilter>(DurationFilter(1, 16))
            }

            disabilityFriendlySwitch.setOnCheckedChangeListener { check, state ->
                when (state) {
                    true -> disabilityFriendlyTextviewSub.text =
                        "Accessibile a persone con disabilità"
                    else -> disabilityFriendlyTextviewSub.text =
                        "Non accessibile a persone con disabilità"
                }
            }
        }
    }
}