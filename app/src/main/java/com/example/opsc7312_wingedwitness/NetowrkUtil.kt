package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
import android.net.Uri
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

///----------------------------------------------------------------------------------------------///

private val PARAM_API_KEY = "key"
private val LOGGING_TAG = "URLWECREATED"
private val EBIRD_REGIONAL_URL = "https://api.ebird.org/v2/data/obs/ZA-WC/recent"

///----------------------------------------------------------------------------------------------///
///Building the URL for Regional
fun buildURLForEbirdRegional(): URL? {
    val buildUri: Uri = Uri.parse(EBIRD_REGIONAL_URL).buildUpon()
        .appendQueryParameter(
            PARAM_API_KEY,
            BuildConfig.EBIRD_API_KEY
        ) // passing in API key
        .build()
    var url: URL? = null
    try {
        url = URL(buildUri.toString())
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
    Log.i(LOGGING_TAG, "buildURLForEbirdRegional: $url")
    return url
}

///----------------------------------------------------------------------------------------------///
///Building the URL for EBird
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

///----------------------------------------------------------------------------------------------///
///Building the URL for Info
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

///----------------------------------------------------------------------------------------------///
///Building the URL for the list in WC
fun getSpeciesListForWesternCape(): List<String> {
    val speciesList = mutableListOf<String>()

    val url = buildURLForEbirdRegional()

    try {
        val urlConnection = url?.openConnection() as HttpURLConnection
        try {
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = StringBuilder()
            reader.forEachLine { result.append(it) }

            Log.d(LOGGING_TAG, "Response: $result")

            val jsonArray = JSONArray(result.toString())
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                speciesList.add(jsonObject.getString("comName"))
            }
        } catch (e: Exception) {
            Log.e(LOGGING_TAG, "Error reading from stream", e)
            e.printStackTrace()
        } finally {
            urlConnection.disconnect()
        }
    } catch (e: Exception) {
        Log.e(LOGGING_TAG, "Error connecting to URL", e)
        e.printStackTrace()
    }

    return speciesList
}
///---------------------------------------EndOfFile----------------------------------------------///

