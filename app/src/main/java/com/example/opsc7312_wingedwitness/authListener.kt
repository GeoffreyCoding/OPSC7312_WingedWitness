package com.example.opsc7312_wingedwitness
/*-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
-----------------------------------------------*/
//---------------------------------------------------------------------------------------------------------------------//
//Imports
import com.google.firebase.auth.FirebaseAuth

class AuthListener {

    //-----------------------------------------------------------------------------------------------------------------//
    //Declarations
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    //-----------------------------------------------------------------------------------------------------------------//
    //Function to start listening
    fun startListening(onSignedIn: (String) -> Unit, onSignedOut: () -> Unit) {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                // User is signed in, pass the user UID to the callback
                onSignedIn(firebaseUser.uid)
            } else {
                // User is signed out, invoke the signed out callback
                onSignedOut()
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    //-----------------------------------------------------------------------------------------------------------------//
    //Function to stop listening
    fun stopListening() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

}//-------------------------------------...ooo000 END OF CLASS 000ooo...-----------------------------------------------//
