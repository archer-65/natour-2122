package com.unina.natourkt.feature_route.create_route

import android.net.Uri
import com.unina.natourkt.core.util.Difficulty

sealed class CreateRouteEvent {
    // GENERAL
    object Upload : CreateRouteEvent()

    // INFO
    data class EnteredTitle(val title: String) : CreateRouteEvent()
    data class EnteredDescription(val description: String) : CreateRouteEvent()
    data class EnteredDuration(val duration: String) : CreateRouteEvent()
    data class EnteredDisability(val disability: Boolean) : CreateRouteEvent()
    data class EnteredDifficulty(val difficulty: Difficulty) : CreateRouteEvent()

    // MAP
    data class AddedStop(val latitude: Double, val longitude: Double) : CreateRouteEvent()
    data class InsertedGpx(val gpx: Uri) : CreateRouteEvent()
    object CleanStop : CreateRouteEvent()

    // PHOTOS
    data class InsertedPhotos(val photos: List<Uri>) : CreateRouteEvent()
    data class RemovePhoto(val position: Int) : CreateRouteEvent()

    object SearchPlace: CreateRouteEvent()
    object SelectGpx: CreateRouteEvent()
}