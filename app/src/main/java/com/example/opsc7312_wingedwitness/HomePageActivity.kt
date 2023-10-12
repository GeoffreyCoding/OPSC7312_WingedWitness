package com.example.opsc7312_wingedwitness

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private lateinit var buttonHotspots: Button
private lateinit var buttonAddSightings: Button
private lateinit var buttonAccount: Button
private lateinit var buttonQuiz: Button
private lateinit var buttonLogout: Button

class HomePageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        // Getting button views
        val btnGoToSightings = findViewById<Button>(R.id.btnGoToSightings)

        btnGoToSightings.setOnClickListener {
            val intent = Intent(this, AddSightingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}
