package com.unina.natourkt.domain.model

import com.google.android.gms.maps.model.LatLng

data class DirectionsPolyline(
    val points: List<LatLng>
)