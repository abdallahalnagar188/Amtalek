package eramo.amtalek.presentation.ui.main.extension

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsSellBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsSellViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class PropertyDetailsSellFragment : BindingFragment<FragmentPropertyDetailsSellBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsSellBinding::inflate

    private val viewModel: PropertyDetailsSellViewModel by viewModels()

    private val args by navArgs<PropertyDetailsSellFragmentArgs>()
    private val propertyId get() = args.propertyId

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

        requestData()
        fetchData()
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun requestData() {
        viewModel.getPropertyDetails()
    }

    private fun fetchData() {
        fetchPropertyDetailsState()
    }

    private fun fetchPropertyDetailsState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertyDetailsState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            assignData(state.data!!)
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }


    private fun setupImageSliderTop(imagesUrl: List<String>) {
        val list = mutableListOf<CarouselItem>()

        for (i in imagesUrl) {
            list.add(
                CarouselItem(
                    imageUrl = i
                )
            )
        }

        binding.apply {
            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            imageSlider.setIndicator(imageSliderDots)
            imageSlider.setData(list)
        }
    }

    private fun assignData(data: PropertyDetailsModel) {
        binding.apply {

            setupImageSliderTop(data.sliderImages)

            tvPrice.text = getString(R.string.s_egp, formatPrice(data.sellPrice))
            tvTitle.text = data.title
            tvLocation.text = data.location
            tvDate.text = data.datePosted

            // header layout
            tvFinishing.text = data.finishingAvailability
            tvLocation2.text = data.areaLocation
            tvArea.text = getString(R.string.s_meter_square_me_n, formatNumber(data.area))
            tvBedrooms.text = getString(R.string.s_bedroom_count_n, data.bedroomsCount.toString())
            tvBathrooms.text = getString(R.string.s_bathroom_count_n, data.bathroomsCount.toString())


            tvUserName.text = data.brokerName
            tvUserId.text = data.brokerDescription

            Glide.with(requireContext())
                .load(data.brokerImageUrl)
                .placeholder(R.drawable.ic_no_image)
                .into(ivUserImage)

            propertyDetailsLayout.tvPropertyCodeValue.text = data.propertyCode
            propertyDetailsLayout.tvTypeValue.text = data.propertyType
            propertyDetailsLayout.tvAreaValue.text = getString(R.string.s_meter_square, formatNumber(data.area))
            propertyDetailsLayout.tvBedroomsValue.text = data.bedroomsCount.toString()
            propertyDetailsLayout.tvBathroomValue.text = data.bathroomsCount.toString()
            propertyDetailsLayout.tvFurnitureValue.text = data.furniture
            propertyDetailsLayout.tvPaymentValue.text = data.payment
            propertyDetailsLayout.tvFinishingValue.text = data.finishing
            propertyDetailsLayout.tvFloorsValue.text = data.floors.joinToString(", ")
            propertyDetailsLayout.tvFloorValue.text = data.landType

            tvDescriptionValue.text = data.description

            tvRatings.text = getString(R.string.s_ratings, data.comments.size.toString())
        }

    }

    private fun assignFakeData() {

        binding.apply {
            tvPrice.text = "4,000,000 EGP"
            tvTitle.text = "Residential apartment for sale, super lux with elevator"
            tvLocation.text = "Sheikh Zayed , Giza"
            tvDate.text = "8/7/2023"

            tvUserName.text = "Lana Realstate company"
            tvUserId.text = "Lana.Eramo12"

            Glide.with(requireContext())
                .load("https://ih1.redbubble.net/image.1587788291.1660/st,small,507x507-pad,600x600,f8f8f8.jpg")
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