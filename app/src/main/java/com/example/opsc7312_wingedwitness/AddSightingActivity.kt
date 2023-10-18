package com.example.opsc7312_wingedwitness

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.media.MediaRecorder
import android.widget.*
import java.io.IOException


private lateinit var edtSightingName:EditText
private lateinit var edtSightingSpecies:EditText
private lateinit var edtSightingLocation:EditText
private lateinit var edtSightingDate:DatePicker
private lateinit var edtSightingCount:EditText
private lateinit var btnAddSighting:Button
private lateinit var ivRecordBird:ImageView
private lateinit var ivCameraBtn:ImageView
//Audio
private var mediaRecorder: MediaRecorder? = null
private var isRecording = false
private var audioFilePath: String? = null
private var imageFilePath: String? = null//variables for camera utilization

private const val REQUEST_CAMERA_PERMISSION = 100
private const val REQUEST_IMAGE_CAPTURE = 101
private const val REQUEST_RECORD_AUDIO_PERMISSION = 101

class AddSightingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsighting)
        var dataValidation = dataValidation()
        //getting edit box views
        edtSightingName = findViewById(R.id.edtSightingName)
        edtSightingSpecies = findViewById(R.id.edtSpeciesName)
        edtSightingLocation = findViewById(R.id.edtSightingLocation)
        edtSightingDate = findViewById(R.id.dpSightingDate)
        edtSightingCount = findViewById(R.id.edtSightingCount)
        btnAddSighting = findViewById(R.id.btnAddSighting)
        ivCameraBtn = findViewById(R.id.ivCamera)
        ivRecordBird = findViewById(R.id.ivRecordSghting)
        //Allows the user to record the sound of the bird they sighted
        ivRecordBird.setOnClickListener{
            if (!isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
        }
        //button click listener to start the validation and the adding of the data to the object
        ivCameraBtn.setOnClickListener {
            // Check for camera permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Camera permission is granted, open the camera
                openCamera()
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
        btnAddSighting.setOnClickListener{
            //get data
            val sCount :String = edtSightingCount.text.toString()
            val sName = edtSightingName.text.toString()
            val sSpecies = edtSightingSpecies.text.toString()
            val sDate = edtSightingDate.toString()
            val sLocation = edtSightingLocation.toString()
            //validate data
            var isValid = dataValidation.validateSightingInput(sCount,sName, sSpecies ,sLocation)
            //save data if valid
            if(isValid){
                var sightingData = SightingData()
                sightingData.sightingId = 0
                sightingData.userId = 0
                sightingData.sightingCount = sCount.toInt()
                sightingData.sightingLocation = sLocation
                sightingData.sightingDate = sDate
                if(audioFilePath != null){
                    sightingData.audioFilePath = audioFilePath
                }else{
                    sightingData.audioFilePath = ""
                }
                if(imageFilePath != null){
                    sightingData.imageFilePath = imageFilePath
                }else{
                    sightingData.imageFilePath = ""
                }
                GlobalDataClass.SightingDataList.add(sightingData)
                Toast.makeText(this, "Sighting Added successfully!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // Start audio recording
    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request audio recording permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
            return
        }

        // Configure MediaRecorder
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        // Define the output file path (you can change this to your desired location)
        audioFilePath = "${externalCacheDir?.absolutePath}/audio_recording.3gp"
        mediaRecorder?.setOutputFile(audioFilePath)

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Stop audio recording
    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false

        // Now you have the audio file path (audioFilePath) that you can use as needed
        if (audioFilePath != null) {
            // Process the recorded audio or save it to your desired location

        }
    }

    private fun openCamera() {
        // Create an intent to capture an image
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Start the camera activity
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    // Handle permission request results for camera and audio recording
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, open the camera
                    openCamera()
                }
            }
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Audio recording permission granted, start recording
                    startRecording()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    // Handle the result of the camera activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image capture was successful
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                // Save the image file path to your SightingData object
                var tempImageFilePath = imageUri.toString()
                // Assuming sightingData is an instance of SightingData
                imageFilePath = tempImageFilePath
            }

        }
    }

}

