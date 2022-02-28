package com.unina.natourkt.presentation.base.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class PlacesContract : ActivityResultContract<List<Place.Field>, Place>() {

    override fun createIntent(context: Context, input: List<Place.Field>): Intent {
        return Autocomplete
            .IntentBuilder(AutocompleteActivityMode.FULLSCREEN, input)
            .build(context)
    }

    override fun parseResult(resultCode: Int, data: Intent?): Place? {
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.i("PLACES API RESPONSE", "Place: ${place.name}, ${place.latLng}")
                    return place
                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    Log.i("PLACES API ERROR", status.statusMessage.toString())
                }
            }
            Activity.RESULT_CANCELED -> {
            }
        }
        return null
    }
}