package com.unina.natourkt.feature_route.route_details.info

import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.load
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.databinding.FragmentRouteDetailsInfoBinding
import com.unina.natourkt.feature_route.route_details.RouteDetailsFragmentDirections
import com.unina.natourkt.feature_route.route_details.RouteDetailsUiState
import com.unina.natourkt.feature_route.route_details.RouteDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsInfoFragment :
    BaseFragment<FragmentRouteDetailsInfoBinding, RouteDetailsViewModel>() {

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteDetailsInfoBinding.inflate(layoutInflater)

    override fun setupUi() {
        super.setupUi()
        binding.rateRouteFab.setBottomMargin()
        setListeners()
    }

    override fun setListeners() {
        binding.rateRouteFab.setOnClickListener {
            val action =
                RouteDetailsFragmentDirections.actionNavigationRouteDetailsToRateRouteDialogFragment2(
                    viewModel.routeId
                )

            findNavController().navigate(action)
        }
    }

    override fun collectState() {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                bindView(it)
            }
        }
    }

    private fun bindView(uiState: RouteDetailsUiState) = with(binding) {
        uiState.apply {
            route?.let {
                routeTitleTextView.text = it.title
                routeDescription.text = it.description

                disabilityFriendlyTextView.isVisible = it.disabilityFriendly
                disabilityFriendlyImage.isVisible = it.disabilityFriendly

                routeImageSlider.load(it.photos)

                durationTextView.text =
                    getString(R.string.duration_placeholder_details, it.duration.toInt())
            }

            progressBar.isVisible = isLoading
            constraintLayout.isVisible = !isLoading && !isError
            chatButton.isVisible = canContactAuthor
            rateRouteFab.isVisible = canRateRoute

            warningTextView.isVisible = isWarningPresent
            warningImage.isVisible = isWarningPresent
            warningTextView.text = warningText?.asString(requireContext())

            difficultyTextview.text = difficultyText?.asString(requireContext())
        }
    }
}