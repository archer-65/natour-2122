package com.unina.natourkt.core.analytics

import android.os.Bundle

fun Map<String, Any>?.toBundle(): Bundle {
    val bundle = Bundle()
    this?.forEach { (key, value) -> bundle.putString(key, value.toString()) }
    return bundle
}