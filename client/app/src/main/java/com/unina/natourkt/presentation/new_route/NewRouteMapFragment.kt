package com.unina.natourkt.presentation.new_route

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.R
import com.unina.natourkt.common.addCustomMarker
import com.unina.natourkt.common.moveAndZoomCamera
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentNewRouteMapBinding
import com.unina.natourkt.presentation.base.fragment.BaseMapFragment

class NewRouteMapFragment :
    BaseMapFragment<FragmentNewRouteMapBinding, NewRouteViewModel, MapView>() {

    private val viewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentNewRouteMapBinding.inflate(layoutInflater)
    override fun getMapBinding() = binding.mapView

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        mapView.setBottomMargin()
        nextFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(topAppBar) {
            setOnMenuItemClickListener {
                onMenuClick(it.itemId)

            }

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        nextFab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_new_route_map_to_newRoutePhotosFragment)
        }
    }

    private fun onMenuClick(itemId: Int): Boolean {
        when (itemId) {
            R.id.search_place -> {
                val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                launcherPlaces.launch(fields)
            }
        }
        return true
    }

    override fun setMapListeners() = with(viewModel) {
        map.setOnMapClickListener {
            addStop(it.latitude, it.longitude)
            getDirections()
        }
    }

    override fun setFirstCameraPosition() = with(viewModel.uiState.value.routeStops.firstOrNull()) {
        val position = this?.let {
            LatLng(it.latitude, it.longitude)
        } ?: LatLng(40.82806233458257, 14.19321142133755)

        map.moveAndZoomCamera(position)
    }

    override fun collectState() = with(viewModel) {
        collectOnLifecycleScope(uiState) {
            if (it.routeStops.isNotEmpty()) {
                it.routeStops.map { stop ->
                    map.addCustomMarker(
                        stop.stopNumber.toString(),
                        LatLng(stop.latitude, stop.longitude)
                    )
                }

                if (it.routeStops.size >= 2) {
                    map.addPolyline(it.polylineOptions)
                }

                binding.nextFab.isEnabled = it.routeStops.size >= 2
            }
        }
    }
}