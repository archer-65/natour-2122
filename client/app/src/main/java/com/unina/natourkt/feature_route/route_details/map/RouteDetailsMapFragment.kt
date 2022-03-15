package com.unina.natourkt.feature_route.route_details.map

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRouteDetailsMapBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseMapFragment
import com.unina.natourkt.core.presentation.util.addCustomMarker
import com.unina.natourkt.core.presentation.util.moveAndZoomCamera
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.feature_route.route_details.RouteDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsMapFragment :
    BaseMapFragment<FragmentRouteDetailsMapBinding, RouteDetailsViewModel, MapView>() {

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteDetailsMapBinding.inflate(layoutInflater)
    override fun getMapBinding() = binding.mapView

    override fun setupUi() {
        mapView.setBottomMargin()
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(uiState) {
            it.route?.let {
                val firstStop = it.stops.first()
                map.moveAndZoomCamera(LatLng(firstStop.latitude, firstStop.longitude))

                it.stops.map { stop ->
                    map.addCustomMarker(
                        stop.stopNumber.toString(),
                        LatLng(stop.latitude, stop.longitude)
                    )
                }

//                if (it.stops.size >= 2) {
//                    getDirections()
                    map.addPolyline(it.polylineOptions)
//                }
            }
        }
    }
}