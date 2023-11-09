package com.example.opsc7312_wingedwitness
import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DBHandler {
    private val db = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth

    // Sign in user
    fun signInUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    callback(true, user?.uid)
                } else {
                    // If sign in fails, display a message to the user.
                    callback(false, task.exception?.message)
                }
            }
    }

    // Register a new user
    fun registerUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    callback(true, user?.uid)
                } else {
                    // If sign in fails, display a message to the user.
                    callback(false, task.exception?.message)
                }
            }
    }

    //add bird to db

    //get bird data from db
}