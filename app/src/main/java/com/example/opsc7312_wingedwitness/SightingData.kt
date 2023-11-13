package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.media.Image
import android.os.Parcel
import android.os.Parcelable
import java.util.Date
//---------------------------------------------------------------------------------------------------------------------//

class SightingData(
    var sightingId: Int = 0,
    var userId: String = "",
    var sightingName: String = "",
    var sightingSpecies: String = "",
    var sightingCount: Int = 0,
    var sightingDate: String = "",
    var sightingLocation: String = "",
    var imageFilePath: String? = null,
    var audioFilePath: String? = null,
    var sightingLat: Double = 0.0,
    var sightingLng: Double = 0.0
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(sightingId)
        dest.writeString(userId)
        dest.writeString(sightingName)
        dest.writeString(sightingSpecies)
        dest.writeInt(sightingCount)
        dest.writeString(sightingDate)
        dest.writeString(sightingLocation)
        dest.writeString(imageFilePath)
        dest.writeString(audioFilePath)
        dest.writeDouble(sightingLat)
        dest.writeDouble(sightingLng)
    }

    companion object CREATOR : Parcelable.Creator<SightingData> {
        override fun createFromParcel(parcel: Parcel): SightingData {
            return SightingData(parcel)
        }

        override fun newArray(size: Int): Array<SightingData?> {
            return arrayOfNulls(size)
        }
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//