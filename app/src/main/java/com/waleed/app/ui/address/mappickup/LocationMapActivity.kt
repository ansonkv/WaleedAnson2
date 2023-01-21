package com.waleed.app.ui.address.mappickup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.location_map_content.*
import kotlinx.android.synthetic.main.toolbar_address.*
import kotlinx.android.synthetic.main.toolbar_address.btn_close
import kotlinx.android.synthetic.main.toolbar_home.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

class LocationMapActivity : BaseActivity(), LocationMapView, OnMapReadyCallback,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveCanceledListener,
    GoogleMap.OnCameraIdleListener,
    PermissionListener {
    companion object {
        const val REQUEST_CHECK_SETTINGS = 43
        const val CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE = 53
        const val DESTINATION_PLACE_AUTOCOMPLETE_REQUEST_CODE = 63
    }

    @Inject
    lateinit var presenter: LocationMapPresenter
    private lateinit var latLocation: LatLng
    private lateinit var marker: Marker
    private var mMap: GoogleMap? = null
    private var mLocationRequest: LocationRequest? = null
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mLat: String? = null
    private var mLon: String? = null

    private   var addressArea: String=""
    private   var addressGovernorate: String=""
    private   var addressStreet: String=""
    private   var addressDirection: String=""
    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_map_address)
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        latLocation = LatLng(29.3759, 47.9774)
        givePermission()
        mapview.onCreate(savedInstanceState)
        mapview.getMapAsync(this)
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapview.onSaveInstanceState(outState!!)
    }

    private fun initViews() {
        btn_close.setOnClickListener { finish() }

        btn_confirm_location.setOnClickListener {
            var intent = Intent()
            if (!addressArea.isNullOrEmpty()) {
                intent.putExtra(AppConstants.BUNDLE_MAP_AREA, addressArea)
            }
            if (!addressGovernorate.isNullOrEmpty()) {
                intent.putExtra(AppConstants.BUNDLE_MAP_GOV, addressGovernorate)
            }
            if (!addressStreet.isNullOrEmpty()) {
                intent.putExtra(AppConstants.BUNDLE_MAP_STREET, addressStreet)
            }
            if (!addressDirection.isNullOrEmpty()) {
                intent.putExtra(AppConstants.BUNDLE_MAP_DIRECTION, addressDirection)
            }
            intent.putExtra(AppConstants.BUNDLE_MAP_LATITUDE, selectedLatitude.toString())
            intent.putExtra(AppConstants.BUNDLE_MAP_LONGITUDE, selectedLongitude.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapview!!.onLowMemory()
    }

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0 ?: return
        if (isPermissionGiven()) {

            mMap!!.setOnCameraIdleListener(this)
            mMap!!.setOnCameraMoveStartedListener(this)
            mMap!!.setOnCameraMoveListener(this)
            mMap!!.setOnCameraMoveCanceledListener(this)
            mMap!!.isMyLocationEnabled = false
            mMap!!.uiSettings.isMyLocationButtonEnabled = false
            mMap!!.uiSettings.isZoomControlsEnabled = false

            getCurrentLocation()
        } else {
            givePermission()
        }
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates.isLocationPresent) {
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    val mLastLocation = task.result
//                    setStartLocation(mLastLocation!!.latitude, mLastLocation.longitude, "")
                    if (mMap != null) {
                        showMarker(mLastLocation!!.latitude, mLastLocation!!.longitude)
                    }
                } else {
                    Toast.makeText(this, "No current location found", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showMarker(latitude: Double, longitude: Double) {
        mMap!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                18.0f
            ), 2000, null
        )
        mMap!!.uiSettings.isCompassEnabled = false
    }

    public override fun onResume() {
        super.onResume()
        mapview!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapview!!.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapview!!.onDestroy()
        mMap!!.clear()
        mMap = null
    }

    override fun onCameraMoveStarted(p0: Int) {
        tv_change_address.makeGone()
        btn_confirm_location.makeGone()
    }

    override fun onCameraMove() {
        latLocation = mMap!!.projection.visibleRegion.latLngBounds.center
        selectedLatitude = latLocation.latitude
        selectedLongitude = latLocation.longitude
    }

    override fun onCameraMoveCanceled() {
    }

    override fun onCameraIdle() {
        tv_change_address.makeVisible()
        var addressString: String = ""
        val gcd = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(latLocation.latitude, latLocation.longitude, 1)
            if (addresses.isNotEmpty()) {
                addressString = addresses[0].getAddressLine(0)
            }
            if (addresses[0].subLocality != null) {
                addressArea = addresses[0].subLocality
            }
            if (addresses[0].adminArea != null) {
                addressGovernorate = addresses[0].adminArea
            }
            if (addresses[0].thoroughfare != null) {
                addressStreet = addresses[0].thoroughfare
            }
            if (addresses[0].featureName != null) {
                addressDirection = addresses[0].featureName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        tv_change_address.text = addressString

        tv_change_address.makeVisible()
        btn_confirm_location.makeVisible()
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        getCurrentLocation()
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        p1!!.continuePermissionRequest()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        Toast.makeText(this, "Permission required for showing location", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun givePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }

    private fun isPermissionGiven(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}