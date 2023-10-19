package com.example.opsc7312_wingedwitness

//---------------------------------------------------------------------------------------------------------------------//
//Imports
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
//---------------------------------------------------------------------------------------------------------------------//

//---------------------------------------------------------------------------------------------------------------------//
//Declarations
private lateinit var buttonHotspots: Button
private lateinit var buttonAddSightings: Button
private lateinit var buttonAccount: Button
private lateinit var buttonQuiz: Button
private lateinit var buttonLogout: Button

class HomePageActivity : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        //FindViews
        val btnGoToHotSpotsMap = findViewById<Button>(R.id.btnHotspots)
        val btnGoToSightings = findViewById<Button>(R.id.btnGoToSightings)
        val btnGoToQiz = findViewById<Button>(R.id.btnPlayGame)
        val btnGoToAccount = findViewById<Button>(R.id.btnAccount)

        //-------------------------------------------------------------------------------------------------------------//
        //Go to Map
        btnGoToHotSpotsMap.setOnClickListener {
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
