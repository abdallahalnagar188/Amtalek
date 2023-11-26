package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsRentBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class PropertyDetailsRentFragment : BindingFragment<FragmentPropertyDetailsRentBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsRentBinding::inflate

    @Inject
    lateinit var rvRatingAdapter: RvRatingAdapter

    @Inject
    lateinit var rvSimilarPropertiesAdapter: RvSimilarPropertiesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        initCommentsRv()
        initSimilarPropertiesRv(Dummy.dummyMyFavouritesList(requireContext()))
        assignFakeData()
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupImageSliderTop(data: List<CarouselItem>) {
        binding.apply {
            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            imageSlider.setIndicator(imageSliderDots)
            imageSlider.setData(data)
        }
    }

    private fun assignFakeData() {
        setupImageSliderTop(Dummy.dummyCarouselPropertiesImagesList())

        binding.apply {
            tvPrice.text = "4000 EGP / Month"
            tvTitle.text = "Residential apartment for sale, super lux with elevator"
            tvLocation.text = "Sheikh Zayed , Giza"
            tvDate.text = "8/7/2023"

            tvUserName.text = "Lana Realstate company"
            tvUserId.text = "Lana.Eramo12"

            Glide.with(requireContext()).load("https://ih1.redbubble.net/image.1587788291.1660/st,small,507x507-pad,600x600,f8f8f8.jpg")
                .into(ivUserImage)

            propertyDetailsLayout.tvPropertyCodeValue.text = "1525898"
            propertyDetailsLayout.tvTypeValue.text = "Town House"
            propertyDetailsLayout.tvAreaValue.text = "188 sqft"
            propertyDetailsLayout.tvBedroomsValue.text = "2"
            propertyDetailsLayout.tvBathroomValue.text = "2"
            propertyDetailsLayout.tvFurnitureValue.text = "Not Available"
            propertyDetailsLayout.tvPaymentValue.text = "Cash Available"
            propertyDetailsLayout.tvFinishingValue.text = "Finishing half"
            propertyDetailsLayout.tvFloorsValue.text = "4.5.6.7"
            propertyDetailsLayout.tvFloorValue.text = "Ceramic"

            tvDescriptionValue.text =
                "Descriptive Text is a text which says what a person or a thing is like. Its purpose is to describe and reveal a particular person, place, or thing."

            tvRatings.text = getString(R.string.s_ratings, "12")
        }

    }

    private fun initCommentsRv() {
        binding.rvRatingComments.adapter = rvRatingAdapter
        rvRatingAdapter.submitList(Dummy.dummyRatingCommentsList())


    }

    private fun initSimilarPropertiesRv(data: List<MyFavouritesModel>) {
        binding.rvSimilarProperties.adapter = rvSimilarPropertiesAdapter
        rvSimilarPropertiesAdapter.submitList(data)
    }
}