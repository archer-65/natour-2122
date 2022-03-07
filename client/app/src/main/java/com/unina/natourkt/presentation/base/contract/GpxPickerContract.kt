package com.unina.natourkt.presentation.base.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class GpxPickerContract : ActivityResultContract<Unit, Uri>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/*"
        }
    }

    override fun parseResult(resultCode: Int, data: Intent?): Uri? {
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.let {
                    val uri = data.data
                    return uri
                }
            }
            Activity.RESULT_CANCELED -> {
            }
        }
        return null
    }
}