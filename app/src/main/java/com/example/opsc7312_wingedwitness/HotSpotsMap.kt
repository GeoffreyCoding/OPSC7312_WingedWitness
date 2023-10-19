package com.example.opsc7312_wingedwitness

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.opsc7312_wingedwitness.databinding.ActivityHotSpotsMapBinding
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.Style.Companion.MAPBOX_STREETS
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import java.lang.Math.*
import java.lang.ref.WeakReference
import kotlin.concurrent.thread
import com.example.opsc7312_wingedwitness.SightingData as SightingData

class HotSpotsMap : AppCompatActivity() {

    private var mapView: MapView? = null
    private var emulatorLatitude: Double = 0.0
    private var emulatorLongitude: Double = 0.0

    private var dist: Double = 2.0

    private var progressDialog: ProgressDialog? = null

    private val mapBoxHelper = HotSpotHelper()

    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private var birdHotSpotList = mutableListOf<ToolBox.BirdHotspots>()


    private val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()

    private lateinit var myButton: ImageButton

    private lateinit var userButton: ImageButton

    private lateinit var backButton: ImageButton

    private val navigationLocationProvider = NavigationLocationProvider()

    private val locationObserver = object : LocationObserver {
        /**
         * Invoked as soon as the [Location] is available.
         */
        override fun onNewRawLocation(rawLocation: android.location.Location) {
            // Not implemented in this example. However, if you want you can also
            // use this callback to get location updates, but as the name suggests
            // these are raw location updates which are usually noisy.
        }

        /**
         * Provides the best possible location update, snapped to the route or
         * map-matched to the road if possible.
         */
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                enhancedLocation,
                locationMatcherResult.keyPoints,
            )
            // Invoke this method to move the camera to your current location.
            updateCamera(enhancedLocation)
        }
    }

    private lateinit var binding: ActivityHotSpotsMapBinding

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().center(it).build())
        mapView?.gestures?.focalPoint = mapView?.getMapboxMap()?.pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }


    ///------------------------------------------------------------------------------------------///

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotSpotsMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {

            progressDialog = ProgressDialog(this)
            progressDialog?.setMessage("Loading...") // Set your loading message
            progressDialog?.setCancelable(false)

            mapView = findViewById(R.id.mapView)
            setupDistanceSpinner(this.mapView!!)
            mapView?.getMapboxMap()?.loadStyleUri(MAPBOX_STREETS) {

                onMapReady()
                mapView?.location?.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                }
            }

            backButton = findViewById(R.id.btnBack)
            backButton.setOnClickListener {
                val intent = Intent(this, HomePageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            userButton = findViewById(R.id.userLocation)
            userButton.setOnClickListener {
                mapView?.camera?.easeTo(
                    CameraOptions.Builder()
                        // Centers the camera to the lng/lat specified.
                        .center(Point.fromLngLat(emulatorLongitude, emulatorLatitude))
                        // specifies the zoom value. Increase or decrease to zoom in or zoom out
                        .zoom(14.0)
                        // specify frame of reference from the center.
                        .padding(EdgeInsets(500.0, 0.0, 0.0, 0.0))
                        .build(),
                )
            }

            myButton = findViewById(R.id.fabLocation)
            myButton.setOnClickListener {
                ///ClearBirdPointAnnotation(mapView)
                // Show the ProgressDialog
                progressDialog?.show()

                thread {
                    val bird = try {
                        buildURLForEbird(
                            emulatorLatitude.toFloat(),
                            emulatorLongitude.toFloat(),
                            dist
                        )?.readText()
                    } catch (e: Exception) {
                        return@thread
                    }

                    runOnUiThread { consumeHotSpotsBirdJson(bird) }
                    progressDialog?.dismiss()

                }
            }
        }

    }

    ///------------------------------------------------------------------------------------------///
    /// When the map is created.
    private fun onMapReady() {
        mapView?.getMapboxMap()?.setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )

        mapView?.getMapboxMap()?.loadStyleUri(MAPBOX_STREETS) {
            initLocationComponent()
            setupGesturesListener()
        }
    }

    ///------------------------------------------------------------------------------------------///
    // set-up the spinner for the hotspots within proximity

    private fun setupDistanceSpinner(mapboxMap: MapView) {

        val distance = findViewById<Spinner>(R.id.userDistance)
        val distanceItems = arrayOf(2, 5, 10, 20, 30, 50)
        val distanceAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, distanceItems)
        distance.adapter = distanceAdapter

        // Set up the OnItemSelectedListener
        distance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Retrieve the selected value from the spinner and set it to the dist variable
                dist = distanceItems[position].toDouble()


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                dist = 2.0
            }
        }
    }

    ///------------------------------------------------------------------------------------------///

    private fun findBirdsWithinRadius(
        markerCoordinateslat: Double,
        markerCoordinatesng: Double,
        callback: (List<ToolBox.TestBird>) -> Unit
    ) {
        // Make an asynchronous network request to fetch bird data

        thread {
            val birdData = try {
                val birdURL = buildURLForAllEbirdInfo(
                    markerCoordinatesng.toFloat(),
                    markerCoordinateslat.toFloat()
                )
                birdURL?.readText()
            } catch (e: Exception) {
                null
            }

            runOnUiThread {
                if (!birdData.isNullOrBlank()) {
                    // Parse the bird data here
                    val parsedBirds = parseBirdData(birdData)
                    callback(parsedBirds)
                } else {
                    callback(emptyList())
                }
            }
        }
    }

    ///------------------------------------------------------------------------------------------///

    private fun parseBirdData(birdData: String): List<ToolBox.TestBird> {
        val gson = Gson()
        val listType = object : TypeToken<List<ToolBox.TestBird>>() {}.type
        return gson.fromJson(birdData, listType)
    }

    ///------------------------------------------------------------------------------------------///
    // Consume the ebird api json and add to a list

    private fun consumeHotSpotsBirdJson(birdJSON: String?) {
        val birds = parseBirdJSON(birdJSON)
        addBirdPointAnnotation(birds)
    }

    ///------------------------------------------------------------------------------------------///
    private fun parseBirdJSON(birdJSON: String?): List<ToolBox.Birds> {
        val mutableBirdList = mutableListOf<ToolBox.Birds>()

        if (!birdJSON.isNullOrEmpty()) {
            birdJSON.trimIndent()

            mutableBirdList.addAll(birdJSON.lines().mapNotNull { line ->
                val parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                if (parts.size >= 9) {
                    ToolBox.Birds(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4].toFloat(),
                        parts[5].toFloat(),
                        parts[6],
                        parts[7],
                        parts[8].toInt()
                    )
                } else {
                    null
                }
            })
        }

        return mutableBirdList
    }

    ///------------------------------------------------------------------------------------------///
    // This function can be called whenever you want to add a bird point annotation
    private fun addBirdPointAnnotation(birds: List<ToolBox.Birds>) {

        for (bird in birds) {
            val myBirdHotspots = ToolBox.BirdHotspots(bird.name, bird.latitude, bird.longitude)
            birdHotSpotList.add(myBirdHotspots)

            // Replace the previous code with the addBirdPointAnnotation function
            mapBoxHelper.addBirdPointAnnotation(mapView!!, bird) { markerCoordinates ->
                // Handle the bird marker click event here
                findBirdsWithinRadius(
                    markerCoordinates.latitude,
                    markerCoordinates.longitude
                ) { matchingBirds ->
                    if (matchingBirds.isNotEmpty()) {
                        // Handle the case when matching birds are found
                        showMarkerFragment(
                            bird.name,
                            markerCoordinates,
                            LatLng(emulatorLatitude, emulatorLongitude),
                            matchingBirds
                        )
                    } else {
                        // Handle the case when no matching birds are found
                        Toast.makeText(this, "Marker Clicked: ${bird.name}", Toast.LENGTH_SHORT)
                            .show()
                        createMarkerFragmentWithoutBirds(
                            bird.name,
                            markerCoordinates,
                            LatLng(emulatorLatitude, emulatorLongitude)
                        )
                    }
                }
            }
        }
    }


///------------------------------------------------------------------------------------------///
/// Display marker fragment

    private fun showMarkerFragment(
        markerName: String,
        markerCoordinates: LatLng,
        emulatorCoordinates: LatLng,
        matchingBirds: List<ToolBox.TestBird>
    ) {
        val fragment = markerName?.let { it1 ->
            MarkerFragment.newInstance(it1, markerCoordinates, emulatorCoordinates, matchingBirds)
        }

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun createMarkerFragmentWithoutBirds(
        markerName: String,
        markerCoordinates: LatLng,
        emulatorCoordinates: LatLng
    ) {
        val fragment = markerName?.let { it1 ->
            MarkerFragment.newInstanceWithoutBirds(it1, markerCoordinates, emulatorCoordinates)
        }

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

///------------------------------------------------------------------------------------------///

    private fun setupGesturesListener() {
        mapView?.gestures?.addOnMoveListener(onMoveListener)
    }

///------------------------------------------------------------------------------------------///

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView?.location
        locationComponentPlugin?.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@HotSpotsMap,
                    R.drawable.ic_my_location,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@HotSpotsMap,
                    R.drawable.user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin?.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin?.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
    }

///------------------------------------------------------------------------------------------///

    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "Free roaming", Toast.LENGTH_SHORT).show()
        mapView?.location
            ?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView?.location
            ?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView?.gestures?.removeOnMoveListener(onMoveListener)
    }

///------------------------------------------------------------------------------------------///

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

///------------------------------------------------------------------------------------------///

    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onResumedObserver = object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.startTripSession()
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterLocationObserver(locationObserver)
            }
        },
        onInitialize = this::initNavigation
    )

///------------------------------------------------------------------------------------------///

    private fun initNavigation() {
        MapboxNavigationApp.setup(
            NavigationOptions.Builder(this)
                .accessToken(getString(R.string.mapbox_access_token))
                .build()
        )
        // Instantiate the location component which is the key component to fetch location updates.
        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)

            // When true, the blue circular puck is shown on the map. If set to false, user
            // location in the form of puck will not be shown on the map.
            enabled = true
        }
    }

///------------------------------------------------------------------------------------------///

    private fun updateCamera(location: android.location.Location) {

        mapView?.camera?.easeTo(
            CameraOptions.Builder()
                // specifies the zoom value. Increase or decrease to zoom in or zoom out
                .zoom(12.0)
                // specify frame of reference from the center.
                .padding(EdgeInsets(500.0, 0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptions
        )
        emulatorLatitude = location.latitude
        emulatorLongitude = location.longitude
    }

    ///------------------------------------------------------------------------------------------///
/// Map stuff
    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        this.mapView?.onStart()

    }

    @SuppressLint("Lifecycle")
    override fun onStop() {
        super.onStop()
        this.mapView?.onStop()
    }

    @SuppressLint("Lifecycle")
    override fun onLowMemory() {
        super.onLowMemory()
        this.mapView?.onLowMemory()
    }

    @SuppressLint("Lifecycle")
    override fun onDestroy() {
        super.onDestroy()
        this.mapView?.onDestroy()
        this.mapView?.location
            ?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        this.mapView?.location
            ?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        this.mapView?.gestures?.removeOnMoveListener(onMoveListener)
    }
///------------------------------------------------------------------------------------------///

}