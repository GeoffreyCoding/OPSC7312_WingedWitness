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
import android.app.DatePickerDialog
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.media.MediaRecorder
import android.widget.*
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


private lateinit var edtSightingName:EditText
private lateinit var edtSightingSpecies:EditText
private lateinit var edtSightingLocation:EditText
private lateinit var edtSightingDate:EditText
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
private val REQUEST_LOCATION_PERMISSION = 1

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

        edtSightingDate.setOnClickListener {
            showDatePickerDialog()
        }

        edtSightingLocation.setOnClickListener {
            getCurrentLocation()
        }


        btnAddSighting.setOnClickListener{
            //get data
            val sCount :String = edtSightingCount.text.toString()
            val sName = edtSightingName.text.toString()
            val sSpecies = edtSightingSpecies.text.toString()
            val sDate = edtSightingDate.text.toString()
            val sLocation = edtSightingLocation.text.toString()
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
                sightingData.sightingName = sName
                sightingData.sightingSpecies = sSpecies
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



                Toast.makeText(this, "Sighting Added", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomePageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

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
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
            return
        }

        // Generate a unique ID for the audio file
        val uniqueId = UUID.randomUUID().toString()

        // Configure MediaRecorder
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        // Define the output file path with a unique ID
        audioFilePath = "${externalCacheDir?.absolutePath}/audio_$uniqueId.3gp"
        mediaRecorder?.setOutputFile(audioFilePath)

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("MediaRecorder", "Error preparing or starting recording", e)
            releaseMediaRecorder() // Release the MediaRecorder in case of an error
        }
        ivRecordBird.setImageResource(R.drawable.pause)
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

        releaseMediaRecorder() // Release the MediaRecorder after stopping recording
        ivRecordBird.setImageResource(R.drawable.play)
    }

    // Release MediaRecorder
    private fun releaseMediaRecorder() {
        mediaRecorder?.release()
        mediaRecorder = null
    }


    private fun openCamera() {
        // Create an intent to capture an image
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Start the camera activity
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    //----------------------------------------------------------------------------------------------------------------//
    //All of the Location Code
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getCurrentLocation() {
        if (checkLocationPermission()) {
            // Get the location manager
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            // Get the last known location
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location == null) {
                //val latitude = location.latitude
                //val longitude = location.longitude

                val latitude = -34.1337
                val longitude = 18.4241

                // Update the UI or use the location data as needed
                val address = getLocationAddress(latitude, longitude)
                edtSightingLocation.setText(address)
            } else {
                // Handle the case where the location is not available
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Request location permission if not granted
            requestLocationPermission()
        }
    }

    private fun getLocationAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            address.getAddressLine(0) ?: ""
        } else {
            "Address not found"
        }
    }


    //----------------------------------------------------------------------------------------------------------------//



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
            val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap?
            if (imageBitmap != null) {
                // Save the image file path to your SightingData object
                imageFilePath = saveImageToExternalCache(imageBitmap)
            }
        }
    }

    private fun saveImageToExternalCache(bitmap: Bitmap): String {
        val directory = File(externalCacheDir, "app_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                updateDate(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }


    private fun updateDate(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDate = dateFormat.format(calendar.time)

        edtSightingDate.setText(selectedDate)

    }




}

