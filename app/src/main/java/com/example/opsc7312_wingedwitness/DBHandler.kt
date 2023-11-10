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


    fun signUpUser(email: String, password: String, metricOrImperial: String, callback: (Boolean, String) -> Unit) {
        // First, create the user account with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Account creation successful, now get the user UID
                    val userUID = auth.currentUser?.uid
                    if (userUID != null) {
                        // Now store additional user details in Firestore
                        val userMap = mapOf(
                            "userUID" to userUID,
                            "userEmail" to email,
                            "metricOrImperial" to metricOrImperial
                        )
                        db.collection("users").document(userUID)
                            .set(userMap)
                            .addOnSuccessListener {
                                // User data successfully written to Firestore
                                callback(true, userUID)
                            }
                            .addOnFailureListener { e ->
                                // Failed to write user data to Firestore
                                callback(false, "Failed to store user data: ${e.localizedMessage}")
                            }
                    } else {
                        // User UID is null, which shouldn't happen
                        callback(false, "Error: User UID is null after signup")
                    }
                } else {
                    // Account creation failed
                    callback(false, "Signup failed: ${task.exception?.localizedMessage}")
                }
            }

    }

    //Update current users metricToImerial
    fun updateMetricToImperial(userUID: String, newMetricToImperial: String){
// Query the 'users' collection for documents where 'userUID' field matches the userUID
        db.collection("users").whereEqualTo("userUID", userUID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Assuming 'userUID' is unique, there should only be one matching document
                    val documentSnapshot = querySnapshot.documents.first()
                    // Update the document
                    documentSnapshot.reference.update("metricOrImperial", newMetricToImperial)
                }
            }
    }

    //add bird to db

    //get bird data from db
}