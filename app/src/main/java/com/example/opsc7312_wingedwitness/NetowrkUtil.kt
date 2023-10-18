package com.example.opsc7312_wingedwitness

import android.net.Uri
import android.util.Log
import java.net.MalformedURLException
import java.net.URL

private val EBIRD_URL =
    "https://api.ebird.org/v2/ref/hotspot/geo?lat=-33.9249&lng=18.4241&dist=5.0"
private val PARAM_API_KEY = "key"
private val LOGGING_TAG = "URLWECREATED"

fun buildURLForEbird(lng: Float, lat: Float, dist: Double): URL? {
    val buildUri: Uri = Uri.parse("https://api.ebird.org/v2/ref/hotspot/geo?lat=${lng}&lng=${lat}&dist=${dist}").buildUpon()
        .appendQueryParameter(
            PARAM_API_KEY,
            BuildConfig.EBIRD_API_KEY
        ) // passing in api key
        .build()
    var url: URL? = null
    try {
        url = URL(buildUri.toString())
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
    Log.i(LOGGING_TAG, "buildURLForBird: $url")
    return url
}

fun buildURLForAllEbirdInfo(lng: Float, lat: Float): URL? {
    val buildUri: Uri = Uri.parse("https://api.ebird.org/v2/data/obs/geo/recent?lat=${lat}&lng=${lng}&sort=species&back=30&dist=0.5").buildUpon()
        .appendQueryParameter(
            PARAM_API_KEY,
            BuildConfig.EBIRD_API_KEY
        ) // passing in api key
        .build()
    var url: URL? = null
    try {
        url = URL(buildUri.toString())
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
    Log.i(LOGGING_TAG, "buildURLForAllEbirdInfo: $url")
    return url
}
