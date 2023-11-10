package com.example.opsc7312_wingedwitness

import com.google.firebase.auth.FirebaseAuth

class AuthListener {

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

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

    fun stopListening() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}
