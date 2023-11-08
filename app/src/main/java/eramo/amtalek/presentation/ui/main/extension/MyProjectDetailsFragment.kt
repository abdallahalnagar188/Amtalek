package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentMyProjectDetailsBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class MyProjectDetailsFragment : BindingFragment<FragmentMyProjectDetailsBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyProjectDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        assignFakeData()
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
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