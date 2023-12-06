package eramo.amtalek.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMapBinding
import eramo.amtalek.presentation.viewmodel.SharedViewModel

@AndroidEntryPoint
class MapFragment : BindingFragment<FragmentMapBinding>(), OnMapReadyCallback {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMapBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            cvBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onMapReady(p0: GoogleMap) {

    }

}