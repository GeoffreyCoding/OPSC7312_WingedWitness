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

    // Function to search all documents for a specific userUID
    public fun getUserDataByUserUID(userUID: String, callback: (UserData?) -> Unit) {
        // Query the 'users' collection for documents where 'userId' field matches the userUID
        db.collection("users").whereEqualTo("userUID", userUID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Assuming 'userId' is unique, there should only be one matching document
                    val documentSnapshot = querySnapshot.documents[0]
                    val userData = documentSnapshot.toObject(UserData::class.java)
                    callback(userData)
                } else {
                    // No matching document found
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors, such as network issues
                callback(null)
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