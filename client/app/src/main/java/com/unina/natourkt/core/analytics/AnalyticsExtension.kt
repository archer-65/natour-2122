package com.unina.natourkt.core.analytics

import android.os.Bundle

/**
 * This functions provides a fast way to convert [AnalyticsEvent.params] to [Bundle], useful for
 * `Firebase Analytics`
 */
fun Map<String, Any>?.toBundle(): Bundle {
    val bundle = Bundle()
    this?.forEach { (key, value) -> bundle.putString(key, value.toString()) }
    return bundle
}