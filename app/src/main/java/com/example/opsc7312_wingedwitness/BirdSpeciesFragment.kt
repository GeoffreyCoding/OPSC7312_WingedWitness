package com.example.opsc7312_wingedwitness

//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
//---------------------------------------------------------------------------------------------------------------------//


class BirdSpeciesFragment : DialogFragment() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Method to populate the fragment
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val species = arrayOf(
            "African Penguin",
            "Black Harrier",
            "Cape Sugarbird",
            "Cape Weaver",
            "Karoo Prinia",
            "Malachite Sunbird",
            "Southern Double-collared Sunbird",
            "African Fish Eagle",
            "African Hoopoe",
            "Secretarybird",
            "Orange-breasted Sunbird",
            "Southern Red Bishop",
            "Pied Kingfisher",
            "Helmeted Guineafowl",
            "African Sacred Ibis",
            "Southern Pale Chanting Goshawk",
            "African Spoonbill",
            "Black-headed Heron",
            "Southern Masked Weaver",
            "Cape Glossy Starling",
            "Kelp Gull",
            "African Black Oystercatcher",
            "African Olive Pigeon",
            "Cape Rock Thrush",
            "White-necked Raven",
            "African Darter",
            "Common Ostrich",
            "Crowned Cormorant",
            "Eurasian Curlew",
        )


        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose Bird Species")
            .setSingleChoiceItems(species, -1) { _, which ->
                val selectedSpecies = species[which]
                // Handle the selected species, e.g., update the EditText
                (activity as? AddSightingActivity)?.setSelectedSpecies(selectedSpecies)
                dismiss()
            }

        return builder.create()
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
