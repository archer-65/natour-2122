package com.unina.natourkt.core.presentation.util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun GoogleMap.addCustomMarker(title: String, latLng: LatLng, isDraggable: Boolean = false) {
    this.addMarker(
        MarkerOptions()
            .position(latLng)
            .title(title)
            .draggable(isDraggable)
    )
}

fun GoogleMap.moveAndZoomCamera(latLng: LatLng, zoom: Float = 15F) {
    this.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
}