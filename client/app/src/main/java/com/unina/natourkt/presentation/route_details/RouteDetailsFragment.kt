package com.unina.natourkt.presentation.route_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentPostDetailsBinding
import com.unina.natourkt.databinding.FragmentRouteDetailsBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class RouteDetailsFragment : BaseFragment(), OnMapReadyCallback {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentRouteDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRouteDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We are not using MapFragment anymore, it's useless and negative for performance.
        // Implementing MapView instead of Fragment requires lifecycle management.
        // Here we are creating our MapView and calling onMapReady callback.
        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync {
                googleMap = it
                onMapReady(googleMap)
            }
        }

        setupUi()
    }

    /**
     * Basic settings for UI
     */
    fun setupUi() {
        binding.mapView.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search_place ->
            }
        }
    }

    /**
     * Callback used when the map is ready, could be useful to
     * set listeners or other map-related functions
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("This is Sydney :)")
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    // Here we are overriding lifecycle functions to manage MapView's lifecycle
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
    // End of lifecycle management for MapView

    val getContent = registerForActivityResult(GetContent()) { uri: Uri? ->
        // Handle the returned Uri
    }

}