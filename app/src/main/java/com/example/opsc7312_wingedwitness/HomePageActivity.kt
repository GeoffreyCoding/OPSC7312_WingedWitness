package com.example.opsc7312_wingedwitness

//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
//---------------------------------------------------------------------------------------------------------------------//

class HomePageActivity : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        //FindViews
        val btnGoToSightings = findViewById<Button>(R.id.btnGoToSightings)
        val btnGoToQiz = findViewById<Button>(R.id.btnPlayGame)
        val btnGoToAccount = findViewById<Button>(R.id.btnAccount)
        val btnHotSpots = findViewById<Button>(R.id.btnHotspots)
        //-------------------------------------------------------------------------------------------------------------//
        //Hotspots
        btnHotSpots.setOnClickListener {
            val intent = Intent(this, HotSpotsMap::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        //-------------------------------------------------------------------------------------------------------------//
        //Go to Sightings
        btnGoToSightings.setOnClickListener {
            val intent = Intent(this, AddSightingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //-------------------------------------------------------------------------------------------------------------//
        //Go to Quiz
        btnGoToQiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //-------------------------------------------------------------------------------------------------------------//
        //Go to Account
        btnGoToAccount.setOnClickListener {
            val intent = Intent(this, accounts_page::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
