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
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
//---------------------------------------------------------------------------------------------------------------------//


class OpeningPage2 : AppCompatActivity() {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private val SPLASH_TIME_OUT: Long = 2000 // 2 seconds

    //-----------------------------------------------------------------------------------------------------------------//
    //OnCreate Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening_page2)

        // Use a Handler to delay the redirection
        Handler().postDelayed({
            // Start the next activity after the splash time out
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
