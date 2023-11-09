package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MarkerFragment : Fragment() {

    private val selectedSpeciesList = mutableListOf<String>()
    private val markerCoordinates = arguments?.getParcelable<LatLng>("markerCoordinates")

    ///-----------------------------------------------------------------------------------------------------------------///

    private fun dismissFragment() {
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    ///-----------------------------------------------------------------------------------------------------------------///

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_marker, container, false)
    }

    ///-----------------------------------------------------------------------------------------------------------------///

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userSightedBirds = arguments?.getSerializable("userSightedBirds") as List<SightingData>?
        val matchingBirds = arguments?.getSerializable("matchingBirds") as List<ToolBox.TestBird>?
        val placeName = arguments?.getString("placeName")

        val emulatorCoordinates = arguments?.getParcelable<LatLng>("emulatorCoordinates")
        val btnClose = view.findViewById<ImageButton>(R.id.btn_close)
        // Find the LinearLayout where you want to add the CardViews
        btnClose.setOnClickListener {
            dismissFragment()
        }

        val tvPlaceName = view.findViewById<TextView>(R.id.placeNameTextView)
        tvPlaceName.text = placeName

        val btnNavigate = view.findViewById<ImageButton>(R.id.btn_navigation)
        btnNavigate.setOnClickListener {
            // Handle button click here

            // Create an Intent to start the new activity
            val intent = Intent(requireContext(), Navigation::class.java)
            if (markerCoordinates != null) {
                intent.putExtra("latitude", markerCoordinates.latitude)
                if (emulatorCoordinates != null) {
                    intent.putExtra("emulatitude", emulatorCoordinates.latitude)
                }

            }
            if (markerCoordinates != null) {
                intent.putExtra("longitude", markerCoordinates.longitude)
                if (emulatorCoordinates != null) {
                    intent.putExtra("emulongitude", emulatorCoordinates.longitude)
                }
            }

            startActivity(intent)
        }

        val tvBird = view.findViewById<TextView>(R.id.tv_bird)
        val addBird = view.findViewById<ImageButton>(R.id.addBird)

        if (matchingBirds != null && matchingBirds.isNotEmpty()) {
            // Access the TextView in your fragment layout (assuming you have a TextView with id tv_bird)
            val birdNames = matchingBirds.joinToString("\n") { "Bird Name: ${it.comName}\n" +
                    "Bird Species: ${it.sciName}\n" +
                    "Bird Date: ${it.obsDt}\n" +
                    "Bird Count: ${it.howMany}\n"}
            tvBird.text = birdNames

            addBird.setOnClickListener {
                fetchBirdSpeciesAndShowDialog(matchingBirds)
            }

            // Set the text of the TextView to the bird names
        }else if (GlobalDataClass.SightingDataList != null && GlobalDataClass.SightingDataList.isNotEmpty()) {
            updateUserSightedBirdsData()}
        else {
            setNoBirdsMessage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setNoBirdsMessage() {
        val tvBird = view?.findViewById<TextView>(R.id.tv_bird)
        tvBird?.text = "No birds were recently spotted in this area."
    }

    private fun updateUserSightedBirdsData() {
        val tvBird = view?.findViewById<TextView>(R.id.tv_bird)

        if (GlobalDataClass.SightingDataList.isNotEmpty()) {
            // Access the TextView in your fragment layout (assuming you have a TextView with id tv_bird)
            val birdNames = GlobalDataClass.SightingDataList.joinToString("\n") { "Sighted Bird:\n" +
                    "Bird Name: ${it.sightingName}\n" +
                    "Date: ${it.sightingDate}\n" +
                    "Location: ${it.sightingLocation}\n" }
            tvBird?.text = birdNames
            // Set the text of the TextView to the user-sighted bird data
        } else {
            setNoBirdsMessage()
        }
    }

    private fun showSpeciesDialog(matchingBirds: List<ToolBox.TestBird>) {
        val birdNames = matchingBirds.map { it.comName }.toTypedArray()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Bird Species")
            .setSingleChoiceItems(birdNames, -1) { dialogInterface, which ->
                val selectedSpecies = matchingBirds[which].comName
                addSelectedSpeciesToList(selectedSpecies)

                var sightingData = SightingData()
                sightingData.sightingId = 0
                sightingData.userId = 0
                sightingData.sightingCount = matchingBirds[which].howMany
                sightingData.sightingLocation = matchingBirds[which].locName
                sightingData.sightingDate = currentDate
                sightingData.sightingName = matchingBirds[which].comName
                sightingData.sightingSpecies = matchingBirds[which].sciName
                sightingData.sightingLat = matchingBirds[which].lat.toDouble()
                sightingData.sightingLng = matchingBirds[which].lng.toDouble()
                GlobalDataClass.SightingDataList.add(sightingData)
                dialogInterface.dismiss()
            }
        builder.show()
    }

    private fun addSelectedSpeciesToList(selectedBird: String) {
        selectedSpeciesList.add(selectedBird)
        // You can perform other actions based on the selected species if needed
    }

    // Call this function when you want to show the species dialog
    private fun fetchBirdSpeciesAndShowDialog(matchingBirds: List<ToolBox.TestBird>) {
        showSpeciesDialog(matchingBirds)
    }

    ///-----------------------------------------------------------------------------------------------------------------///

    companion object {
        fun newInstance(placeName: String, markerCoordinates: LatLng, emulatorCoordinates: LatLng, matchingBirds: List<ToolBox.TestBird>): MarkerFragment {
            val fragment = MarkerFragment()
            val args = Bundle()
            args.putString("placeName", placeName)
            args.putParcelable("markerCoordinates", markerCoordinates)
            args.putParcelable("emulatorCoordinates", emulatorCoordinates)
            args.putSerializable("matchingBirds", ArrayList(matchingBirds))
            println(markerCoordinates.toString())
            fragment.arguments = args
            return fragment
        }

        fun newInstanceWithoutBirds(placeName: String, markerCoordinates: LatLng, emulatorCoordinates: LatLng, ): MarkerFragment {
            val fragment = MarkerFragment()
            val args = Bundle()
            args.putString("placeName", placeName)
            args.putParcelable("markerCoordinates", markerCoordinates)
            args.putParcelable("emulatorCoordinates", emulatorCoordinates)
            fragment.arguments = args
            return fragment
        }

        fun newInstanceForUserSightedBirds(
            placeName: String,
            markerCoordinates: LatLng,
            emulatorCoordinates: LatLng
        ): MarkerFragment {
            val fragment = MarkerFragment()
            val args = Bundle()
            args.putString("placeName", placeName)
            args.putParcelable("markerCoordinates", markerCoordinates)
            args.putParcelable("emulatorCoordinates", emulatorCoordinates)
            // Pass the user-sighted bird data to the fragment
            args.putSerializable("userSightedBirds", ArrayList(GlobalDataClass.SightingDataList))
            fragment.arguments = args
            return fragment
        }
    }

    ///-----------------------------------------------------------------------------------------------------------------///
}
//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
