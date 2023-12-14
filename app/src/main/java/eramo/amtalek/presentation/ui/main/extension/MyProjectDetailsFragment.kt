package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMyProjectDetailsBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class MyProjectDetailsFragment : BindingFragment<FragmentMyProjectDetailsBinding>(), OnMapReadyCallback {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyProjectDetailsBinding::inflate

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        setupToolbar()

        mapSetup()

//        val mapFragment = childFragmentManager.findFragmentById(R.id.my_map) as SupportMapFragment
//        mapFragment.getMapAsync(this)


        assignFakeData()

        binding.tvTitle.setOnClickListener {
            findNavController().navigate(R.id.mapFragment, null, navOptionsAnimation())
        }
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }


    private fun mapSetup() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.my_map) as SupportMapFragment
        mapFragment.getMapAsync(this@MyProjectDetailsFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//
        val sydney = LatLng(30.058680306396965, 31.348735526984008)
//
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney")
//        )
//
////        30.058680306396965, 31.348735526984008
////        mMap.addMarker(MarkerOptions()
////            .position(sydney)
////            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun assignFakeData() {
        setupImageSliderTop(Dummy.dummyCarouselPropertiesImagesList())
        setupAutocadImagesSlider(Dummy.dummyCarouselAutocadImagesList())

        binding.apply {
            tvPrice.text = "4000 EGP / Month"
            tvTitle.text = "Residential apartment for sale, super lux with elevator"
            tvLocation.text = "Sheikh Zayed , Giza"
            tvDate.text = "8/7/2023"

            tvUserName.text = "Lana Realstate company"
            tvUserId.text = "Lana.Eramo12"

            Glide.with(requireContext()).load("https://ih1.redbubble.net/image.1587788291.1660/st,small,507x507-pad,600x600,f8f8f8.jpg")
                .into(ivUserImage)

            tvDescriptionValue.text =
                "Descriptive Text is a text which says what a person or a thing is like. Its purpose is to describe and reveal a particular person, place, or thing."

        }
    }

    private fun setupImageSliderTop(data: List<CarouselItem>) {
        binding.apply {
            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            imageSlider.setIndicator(imageSliderDots)
            imageSlider.setData(data)
        }
    }

    private fun setupAutocadImagesSlider(data: List<CarouselItem>) {
        binding.apply {
            autocadDrawingsSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            autocadDrawingsSlider.setIndicator(autocadDrawingsSliderDots)
            autocadDrawingsSlider.setData(data)
        }
    }

}