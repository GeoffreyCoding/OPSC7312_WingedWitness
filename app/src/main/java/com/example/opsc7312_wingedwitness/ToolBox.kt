package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
import java.io.Serializable

class ToolBox {

    ///------------------------------------------------------------------------------------------///

    data class TestBird(
        val speciesCode: String,
        val comName: String,
        val sciName: String,
        val locId: String,
        val locName: String,
        val obsDt: String,
        val howMany: Int,
        val lat: Float,
        val lng: Float,
        val obsValid: Boolean,
        val obsReviewed: Boolean,
        val locationPrivate: Boolean,
        val subId: String,
        val exoticCategory: String,

        ): Serializable

    ///------------------------------------------------------------------------------------------///


    data class Birds (

        val id: String,
        val countryCode: String,
        val regionCode: String,
        val unused1: String,
        val latitude: Float,
        val longitude: Float,
        val name: String,
        val date: String,
        val unused2: Int
    ): Serializable

    ///------------------------------------------------------------------------------------------///


    data class BirdHotspots (
        val locationName: String,
        val latitude: Float,
        val longitude: Float
    )

    ///------------------------------------------------------------------------------------------///

}