package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MarkerFragment : Fragment() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private lateinit var myUser: String
    private val DBHandler = DBHandler()
    private val selectedSpeciesList = mutableListOf<String>()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val currentDate = sdf.format(Date())
    ///-----------------------------------------------------------------------------------------------------------------///

    private fun dismissFragment() {
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
    ///-----------------------------------------------------------------------------------------------------------------///


    //-----------------------------------------------------------------------------------------------------------------//
    //On create View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_marker, container, false)
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //On View Created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Declarations
        val markerCoordinates = arguments?.getParcelable<LatLng>("markerCoordinates")
        val matchingBirds = arguments?.getSerializable("matchingBirds") as List<ToolBox.TestBird>?
        val userSightedBirds = arguments?.getSerializable("userSightedBirds") as List<SightingData>?
        val placeName = arguments?.getString("placeName")
        val tvBird = view.findViewById<TextView>(R.id.tv_bird)
        val addBird = view.findViewById<ImageButton>(R.id.addBird)
        val tvUserBird: TextView = view.findViewById(R.id.tv_userBird)
        val emulatorCoordinates = arguments?.getParcelable<LatLng>("emulatorCoordinates")

        val userSightedBirdsForMarker = GlobalDataClass.SightingDataList.filter { userSightedBird ->
            userSightedBird.sightingLocation == placeName &&
                    userSightedBird.sightingLat == markerCoordinates?.latitude ?: Float &&
                    userSightedBird.sightingLng == markerCoordinates?.longitude ?: Float
        }

        val btnClose = view.findViewById<ImageButton>(R.id.btn_close)
        // Find the LinearLayout where you want to add the CardViews
        btnClose.setOnClickListener {
            dismissFragment()
        }

        val tvPlaceName = view.findViewById<TextView>(R.id.placeNameTextView)
        tvPlaceName.text = placeName

        val btnNavigate = view.findViewById<ImageButton>(R.id.btn_navigation)
        btnNavigate.setOnClickListener {


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

        //-----------------------------------------------------------------------------------------------------------------//
        //Add Bird Listener
        addBird.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext()).apply {
                setTitle("Add Bird")
                setMessage("Where do you want to add the bird?")
                setPositiveButton("Add to Diary") { _, _ ->
                    if (matchingBirds != null) {
                        fetchBirdSpeciesAndShowDialog(matchingBirds)
                    }
                }
                setNegativeButton("Add to Hotspot") { _, _ ->
                    if (markerCoordinates != null) {
                        showAddToHotspotDialog(
                            tvUserBird,
                            placeName.toString(),
                            markerCoordinates.latitude.toFloat(),
                            markerCoordinates.longitude.toFloat()
                        )
                    }
                }
                setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            builder.show()
        }

        if (matchingBirds != null && matchingBirds.isNotEmpty() && markerCoordinates != null) {
            // Filter the user-added birds for the current marker
            val userSightedBirdsForMarker = GlobalDataClass.SightingDataList.filter { userSightedBird ->
                userSightedBird.sightingLocation == placeName &&
                        userSightedBird.sightingLat == markerCoordinates.latitude &&
                        userSightedBird.sightingLng == markerCoordinates.longitude
            }

            if (userSightedBirdsForMarker.isNotEmpty()) {
                // Display only the user-added bird for the current marker
                val userBirdNames = userSightedBirdsForMarker.joinToString("\n") {
                    "Sighted Bird:\n" +
                            "Bird Name: ${it.sightingName}\n" +
                            "Date: ${it.sightingDate}\n" +
                            "Location: ${it.sightingLocation}\n"
                }
                tvUserBird.text = userBirdNames
            }

            // Display birds from the API
            val birdNames = matchingBirds.joinToString("\n") {
                "Bird Name: ${it.comName}\n" +
                        "Bird Species: ${it.sciName}\n" +
                        "Bird Date: ${it.obsDt}\n" +
                        "Bird Count: ${it.howMany}\n"
            }
            tvBird.text = birdNames

        } else if (userSightedBirdsForMarker.isNotEmpty()) {
            // Display only the user-added bird for the current marker
            updateUserSightedBirdsData(placeName, markerCoordinates)
        } else {
            setNoBirdsMessage()
        }
    }

    ///------------------------------------------------------------------------------------------///

    @SuppressLint("SetTextI18n")
    private fun setNoBirdsMessage() {
        val tvBird = view?.findViewById<TextView>(R.id.tv_bird)
        tvBird?.text = "No birds were recently spotted in this area."
    }

    ///------------------------------------------------------------------------------------------///
    ///Method to update iser sighted birds data
    private fun updateUserSightedBirdsData(placeName: String?, markerCoordinates: LatLng?) {
        val tvUserBird = view?.findViewById<TextView>(R.id.tv_userBird)

        // Filter user-sighted birds based on the conditions for the current marker
        val userSightedBirdsForMarker = GlobalDataClass.SightingDataList.filter { userSightedBird ->
            userSightedBird.sightingLocation == placeName &&
                    userSightedBird.sightingLat == markerCoordinates?.latitude &&
                    userSightedBird.sightingLng == markerCoordinates?.longitude
        }

        if (userSightedBirdsForMarker.isNotEmpty()) {
            // Access the TextView in your fragment layout (assuming you have a TextView with id tv_bird)
            val birdNames = userSightedBirdsForMarker.joinToString("\n") {
                "Bird Name: ${it.sightingName}\n" +
                        "Date: ${it.sightingDate}\n" +
                        "Location: ${it.sightingLocation}\n"
            }
            tvUserBird?.text = birdNames
            // Set the text of the TextView to the user-sighted bird data for the current marker
        } else {
            setNoBirdsMessage()
        }
    }

    ///------------------------------------------------------------------------------------------///
    ///Method to show the species dialog
    private fun showSpeciesDialog(matchingBirds: List<ToolBox.TestBird>) {
        val birdNames = matchingBirds.map { it.comName }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Bird Species")
            .setSingleChoiceItems(birdNames, -1) { dialogInterface, which ->
                val selectedSpecies = matchingBirds[which].comName
                addSelectedSpeciesToList(selectedSpecies)

                var sightingData = SightingData()
                sightingData.sightingId = 0
                sightingData.userId = ""
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

    ///------------------------------------------------------------------------------------------///
    /// Method to add the selected species to a list
    private fun addSelectedSpeciesToList(selectedBird: String) {
        selectedSpeciesList.add(selectedBird)
    }

    ///------------------------------------------------------------------------------------------///
    // Call this function when you want to show the species dialog
    private fun fetchBirdSpeciesAndShowDialog(matchingBirds: List<ToolBox.TestBird>) {
        showSpeciesDialog(matchingBirds)
    }

    ///------------------------------------------------------------------------------------------///
    ///Method to add to the hotspot dialog
    private fun showAddToHotspotDialog(user: TextView, place: String, lat: Float, lng: Float) {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle("Add to Hotspot")

            // Create EditText widgets programmatically
            val etBirdName = createEditText("Bird Name")
            val etSpecies = createEditText("Species")
            val etAmount = createAmountEditText("Amount")

            // Add EditText widgets to the dialog
            val layout = createLinearLayout(etBirdName, etSpecies, etAmount)
            setView(layout)

            setPositiveButton("Add") { _, _ ->
                handleAddToHotspot(etBirdName, etSpecies, etAmount, user, place, lat, lng)
            }

            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }

        builder.show()
    }

    ///------------------------------------------------------------------------------------------///
    //Create the edit text
    private fun createEditText(hint: String): EditText {
        val et = EditText(requireContext()).apply {
            this.hint = hint
            layoutParams = createLayoutParams()
        }
        return et
    }

    ///------------------------------------------------------------------------------------------///
    ///Create the amount edit text
    private fun createAmountEditText(hint: String): EditText {
        return createEditText(hint).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    ///------------------------------------------------------------------------------------------///
    ///Create the Linear layout
    private fun createLinearLayout(vararg views: View): LinearLayout {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = createLayoutParams()
            views.forEach { addView(it) }
        }
        return layout
    }

    ///------------------------------------------------------------------------------------------///
    ///Create the layout params
    private fun createLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(40, 20, 40, 0)
        }
    }

    ///------------------------------------------------------------------------------------------///
    ///Method to handle the logic
    private fun handleAddToHotspot(
        etBirdName: EditText, etSpecies: EditText, etAmount: EditText,
        user: TextView, place: String, lat: Float, lng: Float) {
        for (user in GlobalDataClass.UserDataList) {
            myUser = user.userId
        }

        //Declarations
        val birdName = etBirdName.text.toString()
        val species = etSpecies.text.toString()
        val amount = etAmount.text.toString().toIntOrNull()

        if (birdName.isNotEmpty() && species.isNotEmpty() && amount != null) {
            val newSighting = SightingData().apply {
                sightingId = 0
                userId = ""
                sightingLocation = place
                sightingDate = currentDate
                sightingName = birdName
                sightingSpecies = species
                sightingCount = amount
                sightingLat = lat.toDouble()
                sightingLng = lng.toDouble()
            }
            GlobalDataClass.SightingDataList.add(newSighting)
            DBHandler.addBirdToFireBase(
                "",
                "",
                newSighting.sightingCount,
                newSighting.sightingDate,
                newSighting.sightingId.toString(),
                newSighting.sightingLat.toString(),
                newSighting.sightingLng.toString(),
                newSighting.sightingLocation,
                newSighting.sightingName,
                newSighting.sightingSpecies,
                myUser
            ) { success, result ->
                if (success) {
                    Toast.makeText(
                        this.context,
                        result,
                        Toast.LENGTH_SHORT
                    )// result contains the new user's UID
                } else {
                    Toast.makeText(
                        this.context,
                        result,
                        Toast.LENGTH_SHORT
                    )// result contains the error message
                }
            }
        }


        val birdDetails = "Bird Name: $birdName\nBird Date: $currentDate\n" +
                "Bird Species: $species\n" +
                "Bird Count: $amount"
        user.text = birdDetails
    }

    ///------------------------------------------------------------------------------------------///
    /// Companion object to create three types of instances dependent on the marker click.
    companion object {
        fun newInstance(placeName: String, markerCoordinates: LatLng, emulatorCoordinates: LatLng,
                        matchingBirds: List<ToolBox.TestBird>, ): MarkerFragment {
            val fragment = MarkerFragment()
            val args = Bundle()
            args.putString("placeName", placeName)
            args.putParcelable("markerCoordinates", markerCoordinates)
            args.putParcelable("emulatorCoordinates", emulatorCoordinates)
            args.putSerializable("matchingBirds", ArrayList(matchingBirds))
            args.putSerializable("userSightedBirds", ArrayList(GlobalDataClass.SightingDataList))
            println(markerCoordinates.toString())
            fragment.arguments = args
            return fragment
        }

        ///--------------------------------------------------------------------------------------///
        ///If there is a new instance
        fun newInstanceWithoutBirds(placeName: String, markerCoordinates: LatLng,
                                    emulatorCoordinates: LatLng, ): MarkerFragment {
            val fragment = MarkerFragment()
            val args = Bundle()
            args.putString("placeName", placeName)
            args.putParcelable("markerCoordinates", markerCoordinates)
            args.putParcelable("emulatorCoordinates", emulatorCoordinates)
            fragment.arguments = args
            return fragment
        }

        ///--------------------------------------------------------------------------------------///
        ///If there is a new instance
        fun newInstanceForUserSightedBirds(placeName: String, markerCoordinates: LatLng,
            emulatorCoordinates: LatLng): MarkerFragment {
            val fragment = MarkerFragment()
            val args = Bundle()
            args.putString("placeName", placeName)
            args.putParcelable("markerCoordinates", markerCoordinates)
            args.putParcelable("emulatorCoordinates", emulatorCoordinates)
            args.putSerializable("userSightedBirds", ArrayList(GlobalDataClass.SightingDataList))
            fragment.arguments = args
            return fragment
        }
    }

    ///------------------------------------------------------------------------------------------///
}
///------------------------------...ooo000 END OF CLASS 000ooo...--------------------------------///
