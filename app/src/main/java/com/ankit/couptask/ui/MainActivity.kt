package com.ankit.couptask.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.ankit.couptask.R
import com.ankit.couptask.models.Scooter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MainActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    internal lateinit var scooterList: MutableList<Scooter>
    internal lateinit var markerList: MutableList<Marker>
    internal lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
        mapFragment.getMapAsync(this)
        val scooter = Scooter(this, this)
        scooter.getScooterListFromAPI()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Coup - eScooter Sharing Office, Berlin and move the camera.
        val berlin = LatLng(52.519326, 13.3858253)
        mMap!!.addMarker(MarkerOptions()
                .position(berlin)
                .title("Coup Office")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_office)))
        mMap!!.setOnMarkerClickListener { marker -> !marker.title.equals("Coup Office") && onMarkerClickk(marker) }
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(berlin, 12f))
        if (checkLocationPermission())
            mMap!!.isMyLocationEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
    }

    @SuppressLint("SetTextI18n")
    fun onMarkerClickk(arg0: Marker): Boolean {
        if (arg0.snippet == null) {
            mMap!!.moveCamera(CameraUpdateFactory.zoomIn())
            return true
        } else
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(arg0.position, 14f))

        val index = markerList.indexOf(arg0)
        val scooter = scooterList[index]
        val d = Dialog(this@MainActivity)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        d.setContentView(R.layout.info_content)

        var color = R.color.green
        if (scooter.energyLevel > 0 && scooter.energyLevel <= 30)
            color = R.color.red
        else if (scooter.energyLevel > 30 && scooter.energyLevel <= 50)
            color = R.color.yellow
        else if (scooter.energyLevel > 50)
            color = R.color.green

        val batteryText = d.findViewById<TextView>(R.id.battery_)
        batteryText.text = scooter.energyLevel.toString() + " %"
        batteryText.setTextColor(resources.getColor(color))

        val distanceText = d.findViewById<TextView>(R.id.distance_)
        distanceText.text = scooter.distanceCapacity.toString() + " KM"

        val modelText = d.findViewById<TextView>(R.id.model_)
        modelText.text = scooter.scooterModel

        val licenseText = d.findViewById<TextView>(R.id.license_)
        licenseText.text = scooter.licensePlate

        d.show()
        return false
    }

    fun updateScooterList(scooterList: List<Scooter>) {
        this.scooterList = ArrayList<Scooter>()
        markerList = ArrayList<Marker>()
        if (mMap != null)
            for (scooter in scooterList) {
                var resId = R.drawable.ic_green_scooter
                if (scooter.energyLevel > 0 && scooter.energyLevel <= 30)
                    resId = R.drawable.ic_red_scooter
                else if (scooter.energyLevel > 30 && scooter.energyLevel <= 50)
                    resId = R.drawable.ic_yellow_scooter
                else if (scooter.energyLevel > 50)
                    resId = R.drawable.ic_green_scooter
                val marker = createMarker(scooter.scooterLocation.latitude,
                        scooter.scooterLocation.longitude, resId)
                marker.hideInfoWindow()
                markerList.add(marker)
                this.scooterList.add(scooter)
            }
        progressBar.visibility = View.GONE
    }

    protected fun createMarker(latitude: Double, longitude: Double, iconResID: Int): Marker {
        return mMap!!.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title("Selected Scooter")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(iconResID)))
    }

    private fun checkLocationPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    val alertBuilder = AlertDialog.Builder(this)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("Location permission is necessary")
                    alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
                    }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }

    }

    fun errorUpdate() {
        progressBar.visibility = View.GONE
    }

    companion object {

        private val MY_PERMISSIONS_REQUEST_LOCATION = 1101
    }
}
