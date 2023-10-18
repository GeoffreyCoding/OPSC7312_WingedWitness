package com.example.opsc7312_wingedwitness

//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//---------------------------------------------------------------------------------------------------------------------//


class accounts_page : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private lateinit var addEntry: ImageView
    private lateinit var back: ImageView
    private lateinit var recyclerView: RecyclerView

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accounts_page)

        //FindViews
        addEntry = findViewById(R.id.addEntry)
        back = findViewById(R.id.Back)
        recyclerView = findViewById(R.id.recyclerView)

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

        }

}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
