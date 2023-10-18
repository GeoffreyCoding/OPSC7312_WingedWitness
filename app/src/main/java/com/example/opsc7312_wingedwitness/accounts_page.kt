package com.example.opsc7312_wingedwitness

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class accounts_page : AppCompatActivity() {

    private lateinit var addEntry: ImageView
    private lateinit var back: ImageView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accounts_page)

        addEntry = findViewById(R.id.addEntry)
        back = findViewById(R.id.Back)
        recyclerView = findViewById(R.id.recyclerView)

        addEntry.setOnClickListener {
            val intent = Intent(this, AddSightingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Set up a layout manager for the RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Assuming GlobalDataClass.SightingDataList contains your sightings
        val sightings = GlobalDataClass.SightingDataList

        // Create an adapter and set it to the RecyclerView
        val adapter = SightingAdapter(sightings)
        recyclerView.adapter = adapter


        }

}

