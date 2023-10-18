package com.example.opsc7312_wingedwitness

import android.net.Uri
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
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

fun getSpeciesList(lat : Float,lng : Float): List<String> {
    val speciesList = mutableListOf<String>()

    // Use buildURLForAllEbirdInfo as it seems to fetch recent observations
    val url = buildURLForAllEbirdInfo(lng, lat)

    val urlConnection = url?.openConnection() as HttpURLConnection
    try {
        val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
        val result = StringBuilder()
        reader.forEachLine { result.append(it) }

        val jsonArray = JSONArray(result.toString())
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            speciesList.add(jsonObject.getString("comName")) // Assuming "comName" is the field containing the species name
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        urlConnection.disconnect()
    }

    return speciesList
}
