package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//---------------------------------------------------------------------------------------------------------------------//


class accounts_page : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private lateinit var addEntry: ImageView
    private lateinit var back: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rbMetric:RadioButton
    private lateinit var rbImperial:RadioButton

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accounts_page)
        var userData = UserData()
        //FindViews
        addEntry = findViewById(R.id.addEntry)
        back = findViewById(R.id.Back)
        recyclerView = findViewById(R.id.recyclerView)
        rbMetric = findViewById(R.id.radioButtonMetric)
        rbImperial = findViewById(R.id.radioButtonImperial)
        //-------------------------------------------------------------------------------------------------------------//
        //Add Entry
        addEntry.setOnClickListener {
            val intent = Intent(this, AddSightingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //-------------------------------------------------------------------------------------------------------------//
        //Back Button
        back.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //-------------------------------------------------------------------------------------------------------------//
        // Set up a layout manager for the RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val sightings = GlobalDataClass.SightingDataList

        // Create an adapter and set it to the RecyclerView
        val adapter = SightingAdapter(sightings)
        recyclerView.adapter = adapter

        //-------------------------------------------------------------------------------------------------------------//
        //Change metric in userdata
        rbMetric.setOnClickListener{
            userData.metricOrImperial = "metric"
        }
        //-------------------------------------------------------------------------------------------------------------//
        //Change Imperial in userdata
        rbImperial.setOnClickListener{
            userData.metricOrImperial = "imperial"
        }
    }

}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
