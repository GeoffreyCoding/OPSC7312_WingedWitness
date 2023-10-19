package com.example.opsc7312_wingedwitness

//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.R
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//---------------------------------------------------------------------------------------------------------------------//


class BirdSpeciesFragment : DialogFragment() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Method to populate the fragment
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        fetchSpeciesAndShowDialog(builder)
        return builder.create()
    }

    private fun fetchSpeciesAndShowDialog(builder: AlertDialog.Builder) {
        CoroutineScope(Dispatchers.IO).launch {
            val tempSpeciesList = getSpeciesListForWesternCape()
            withContext(Dispatchers.Main) {
                val species = tempSpeciesList.toTypedArray()
                buildAndShowSpeciesDialog(builder, species)
            }
        }
    }

    private fun buildAndShowSpeciesDialog(builder: AlertDialog.Builder, species: Array<String>) {
        builder.setTitle("Choose Bird Species")
            .setSingleChoiceItems(species, -1) { dialogInterface, which ->
                val selectedSpecies = species[which]
                (activity as? AddSightingActivity)?.setSelectedSpecies(selectedSpecies)
                dialogInterface.dismiss()
            }
        builder.show()
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
