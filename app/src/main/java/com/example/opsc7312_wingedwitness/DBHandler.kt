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
    fun getUserDataByUserUID(userUID: String, callback: (UserData?) -> Unit) {
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

    fun addBirdToFireBase(
        audioFilePath: String,
        imageFilePath: String,
        sightingCount: Int,
        sightingDate: String,
        sightingId: String,
        sightingLat: String,
        sightingLng: String,
        sightingLocation: String,
        sightingName: String,
        sightingSpecies: String,
        userUID: String,
        callback: (Boolean, String) -> Unit
    ) {
        // Create a document with a unique ID for the bird sighting
        val birdDocument = db.collection("userBirds").document()

        // Create a map with the bird sighting data
        val birdMap = mapOf(
            "audioFilePath" to audioFilePath,
            "imageFilePath" to imageFilePath,
            "sightingCount" to sightingCount,
            "sightingDate" to sightingDate,
            "sightingId" to sightingId,
            "sightingLat" to sightingLat,
            "sightingLng" to sightingLng,
            "sightingLocation" to sightingLocation,
            "sightingName" to sightingName,
            "sightingSpecies" to sightingSpecies,
            "userUID" to userUID
        )

        // Set the data in Firestore
        birdDocument.set(birdMap)
            .addOnSuccessListener {
                // Bird data successfully written to Firestore
                callback(true, birdDocument.id)
            }
            .addOnFailureListener { e ->
                // Failed to write bird data to Firestore
                callback(false, "Failed to store bird data: ${e.localizedMessage}")
            }
    }

    /*fun getBirdSightingsForUser(userUID: String, callback: (List<SightingData>, String?) -> Unit) {

        // Create a query to retrieve bird sightings for a specific user
        val query = db.collection("userBirds")
            .whereEqualTo("userUID", userUID)

        // Execute the query
        query.get()
            .addOnSuccessListener { result ->
                // Parse the result and convert documents to BirdSighting objects
                val birdSightings = result.documents.map { document ->
                    val audioFilePath = document.getString("audioFilePath") ?: ""
                    val imageFilePath = document.getString("imageFilePath") ?: ""
                    val sightingCount = document.getLong("sightingCount")?.toInt() ?: 0
                    val sightingDate = document.getString("sightingDate") ?: ""
                    val sightingId = document.getString("sightingId") ?: ""
                    val sightingLat = document.getString("sightingLat") ?: ""
                    val sightingLng = document.getString("sightingLng") ?: ""
                    val sightingLocation = document.getString("sightingLocation") ?: ""
                    val sightingName = document.getString("sightingName") ?: ""
                    val sightingSpecies = document.getString("sightingSpecies") ?: ""


                    SightingData(
                        sightingId.toInt(),
                        userUID,
                        sightingName,
                        sightingSpecies,
                        sightingCount,
                        sightingDate,
                        sightingLocation,
                        imageFilePath,
                        audioFilePath,
                        sightingLat.toDouble(),
                        sightingLng.toDouble(),
                    )
                }
                // Pass the list of bird sightings to the callback
                callback(birdSightings, null)
            }
            .addOnFailureListener { e ->
                // Failed to fetch bird sightings
                callback(emptyList(), "Error fetching bird sightings: ${e.localizedMessage}")
            }
    }*/
    fun getBirdSightingsForUser(userUID: String, callback: (List<SightingData>, String?) -> Unit) {

        // Create a query to retrieve bird sightings for a specific user
        val query = db.collection("userBirds")
            .whereEqualTo("userUID", userUID)

        // Execute the query
        query.get()
            .addOnSuccessListener { result ->
                // Parse the result and convert documents to SightingData objects
                val birdSightings = result.documents.map { document ->
                    val audioFilePath = document.getString("audioFilePath") ?: ""
                    val imageFilePath = document.getString("imageFilePath") ?: ""
                    val sightingCount = document.getLong("sightingCount")?.toInt() ?: 0
                    val sightingDate = document.getString("sightingDate") ?: ""
                    val sightingId = document.getString("sightingId") ?: ""
                    val sightingLat = document.getString("sightingLat") ?: ""
                    val sightingLng = document.getString("sightingLng") ?: ""
                    val sightingLocation = document.getString("sightingLocation") ?: ""
                    val sightingName = document.getString("sightingName") ?: ""
                    val sightingSpecies = document.getString("sightingSpecies") ?: ""

                    SightingData().apply {
                        this.sightingId = sightingId.toInt()
                        this.userId = userUID
                        this.sightingName = sightingName
                        this.sightingSpecies = sightingSpecies
                        this.sightingCount = sightingCount
                        this.sightingDate = sightingDate
                        this.sightingLocation = sightingLocation
                        this.imageFilePath = imageFilePath
                        this.audioFilePath = audioFilePath
                        this.sightingLat = sightingLat.toDouble()
                        this.sightingLng = sightingLng.toDouble()
                    }
                }
                // Pass the list of bird sightings to the callback
                callback(birdSightings, null)
            }
            .addOnFailureListener { e ->
                // Failed to fetch bird sightings
                callback(emptyList(), "Error fetching bird sightings: ${e.localizedMessage}")
            }
    }
}