package com.weatherinformation.ui.home.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.weatherinformation.R
import com.example.weatherinformation.databinding.MapFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.weatherinformation.base.BaseFragment
import com.weatherinformation.ui.home.activity.HomeActivity
import com.weatherinformation.utils.AppConstants
import java.util.Locale

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: MapFragmentBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: LatLng

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        initMapView()
        moveOnClickListeners()
    }

    private fun setUpToolBar(){
        (activity as HomeActivity).apply {
            goBack()
            setTitle(getString(R.string.title_choose_location))
        }
    }

    private fun moveOnClickListeners() = with(binding) {
        btnChoose.setOnClickListener {
            val weatherFragment = WeatherInfoFragment()
            val bundle = Bundle().apply {
                putDouble(AppConstants.LAT, currentLocation.latitude)
                putDouble(AppConstants.LONG, currentLocation.longitude)
            }
            weatherFragment.arguments = bundle
            provideLoadFragmentInfo(weatherFragment, isAdd = false, isAddBackStack = true)
        }
    }

    private fun initMapView() = with(binding.mapView) {
        onCreate(null)
        onResume()
        getMapAsync(this@MapFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            // Get the user's current location
            val location = getLastKnownLocation()
            currentLocation = LatLng(location.latitude, location.longitude)
            // Move the camera to the current location
            googleMap.cameraPosition.target
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            googleMap.setOnCameraIdleListener {
                val centerLatLng = googleMap.cameraPosition.target
                currentLocation = centerLatLng
                // Update the position of imgViewMarker based on the centerLatLng
                getAddressFromLatLong(centerLatLng)
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun getLastKnownLocation(): Location {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        return bestLocation ?: Location(LocationManager.GPS_PROVIDER)
    }

    private fun getAddressFromLatLong(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address: Address?
        var fulladdress = ""
        val addresses: List<Address>? =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                address = addresses[0]
                fulladdress =
                    address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
                binding.textViewAddress.text = fulladdress
            } else {
                binding.textViewAddress.text = "Location not found"
            }
        }
    }
}