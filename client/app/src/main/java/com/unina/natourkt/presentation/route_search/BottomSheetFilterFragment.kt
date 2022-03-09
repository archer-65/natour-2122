package com.unina.natourkt.presentation.route_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentBottomSheetFilterBinding
import com.unina.natourkt.presentation.base.contract.PlacesContract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BottomSheetFilterFragment : BottomSheetDialogFragment() {

    /**
     * Activity result launcher for `Places API`
     */
    private lateinit var launcherPlaces: ActivityResultLauncher<List<Place.Field>>

    private val viewModel: RouteSearchViewModel by hiltNavGraphViewModels(R.id.navigation_search_flow)

    private var _binding: FragmentBottomSheetFilterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetFilterBinding.inflate(inflater, container, false)
        initPlacesSearch()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {

    }

    private fun setListeners() {
        binding.areaChip.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            launcherPlaces.launch(fields)
        }

        binding.areaChip.setOnCloseIconClickListener {
            viewModel.setPlace(null)
        }

        binding.areaSlider.addOnChangeListener { _, value, _ ->
            viewModel.setDistance(value)
        }

        binding.durationChipGroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.durationFirst -> {
                    viewModel.setDurationRange(1, 4)
                }
                R.id.durationSecond -> {
                    viewModel.setDurationRange(4, 8)
                }
                R.id.durationThird -> {
                    viewModel.setDurationRange(8, 12)
                }
                R.id.durationFourth -> {
                    viewModel.setDurationRange(12, 16)
                }
                else -> {
                    viewModel.setDurationRange(null, null)
                }
            }
        }

        binding.difficultyChipgroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.minEasyChip -> {
                    viewModel.setDifficulty(Difficulty.EASY)
                }
                R.id.minMediumChip -> {
                    viewModel.setDifficulty(Difficulty.MEDIUM)
                }
                R.id.minHardChip -> {
                    viewModel.setDifficulty(Difficulty.HARD)
                }
                else -> {
                    viewModel.setDifficulty(Difficulty.NONE)
                }
            }
        }

        binding.disabilityFriendlyGroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.accessible -> {
                    viewModel.setDisability(true)
                }
                R.id.notAccessible -> {
                    viewModel.setDisability(false)
                }
                else -> {
                    viewModel.setDisability(null)
                }
            }
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    if (it.place != null) {
                        binding.areaChip.text = it.place.name
                        binding.areaChip.isCloseIconVisible = true
                    } else {
                        binding.areaChip.text = getString(R.string.choose_position)
                        binding.areaChip.isCloseIconVisible = false
                    }

                    binding.areaSlider.isEnabled = it.place != null

                    binding.areaSlider.value = it.distance
                    binding.radiusTextView.text = it.distance.toInt().toString()

                    bindDuration(it.minDuration, it.maxDuration)

                    bindDifficulty(it.minDifficulty)

                    bindDisability(it.isDisabilityFriendly)
                }
            }
        }
    }

    /**
     * Function to initialize [launcherPlaces] with [registerForActivityResult], replacing
     * [startActivityForResult] and [onActivityResult] (deprecated)
     * @see [PlacesContract]
     */
    private fun initPlacesSearch() {
        launcherPlaces = registerForActivityResult(PlacesContract()) { result ->
            result?.let {
                viewModel.setPlace(it)
            }
        }
    }

    private fun bindDuration(minDuration: Int?, maxDuration: Int?) = with(binding) {
        if (minDuration == 1 && maxDuration == 4) {
            durationChipGroup.check(R.id.durationFirst)
        } else if (minDuration == 4 && maxDuration == 8) {
            durationChipGroup.check(R.id.durationSecond)
        } else if (minDuration == 8 && maxDuration == 12) {
            durationChipGroup.check(R.id.durationThird)
        } else if (minDuration == 12 && maxDuration == 16) {
            durationChipGroup.check(R.id.durationFourth)
        } else {
            durationChipGroup.clearCheck()
        }
    }

    private fun bindDifficulty(minDifficulty: Difficulty) = with(binding) {
        when (minDifficulty) {
            Difficulty.EASY -> difficultyChipgroup.check(R.id.minEasyChip)
            Difficulty.MEDIUM -> difficultyChipgroup.check(R.id.minMediumChip)
            Difficulty.HARD -> difficultyChipgroup.check(R.id.minHardChip)
            Difficulty.NONE -> difficultyChipgroup.clearCheck()
        }
    }

    private fun bindDisability(disabilityFriendly: Boolean?) = with(binding) {
        when (disabilityFriendly) {
            true -> disabilityFriendlyGroup.check(R.id.accessible)
            false -> disabilityFriendlyGroup.check(R.id.notAccessible)
            else -> disabilityFriendlyGroup.clearCheck()
        }
    }
}