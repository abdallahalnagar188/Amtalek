package eramo.amtalek.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMapBinding
import eramo.amtalek.presentation.viewmodel.SharedViewModel

@AndroidEntryPoint
class MapFragment : BindingFragment<FragmentMapBinding>(), OnMapReadyCallback {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMapBinding::inflate

    private var locationLatLng: LatLng? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url")
        locationLatLng = extractLatLngFromUrl(url)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            cvBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        locationLatLng?.let { latLng ->
            // Move the camera to the extracted location and add a marker
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        } ?: run {
            // If no valid location, set a default one (Optional)
            val defaultLocation = LatLng(0.0, 0.0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 2f))
        }

        googleMap.uiSettings.isZoomControlsEnabled = true // Enable zoom controls
    }

    private fun extractLatLngFromUrl(url: String?): LatLng? {
        return url?.let {
            try {
                val latLngString = it.removePrefix("geo:") // Remove 'geo:' prefix
                val latLng = latLngString.split(",")
                val latitude = latLng[0].toDouble()
                val longitude = latLng[1].toDouble()
                LatLng(latitude, longitude)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
