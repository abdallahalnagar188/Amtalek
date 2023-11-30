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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsSellAndRentBinding
import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvAmenitiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsSellAndRentViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import eramo.amtalek.util.getYoutubeUrlId
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class PropertyDetailsSellAndRentFragment : BindingFragment<FragmentPropertyDetailsSellAndRentBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsSellAndRentBinding::inflate

    private val viewModel: PropertyDetailsSellAndRentViewModel by viewModels()

    private val args by navArgs<PropertyDetailsRentFragmentArgs>()
    private val propertyId get() = args.propertyId

    @Inject
    lateinit var rvAmenitiesAdapter: RvAmenitiesAdapter

    @Inject
    lateinit var rvRatingAdapter: RvRatingAdapter

    @Inject
    lateinit var rvSimilarPropertiesAdapter: RvSimilarPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        requestData()
        fetchData()
    }

    private fun setupViews() {
        setupToolbar()
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun requestData() {
        viewModel.getPropertyDetails(propertyId)
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

    private fun assignData(data: PropertyDetailsModel) {
        binding.apply {

            setupImageSliderTop(data.sliderImages)

            tvPrice.text = getString(
                R.string.s_sell_price_and_rent_price,
                getString(R.string.s_egp, formatPrice(data.sellPrice)),
                getRentPrice(data.rentDuration, data.rentPrice)
            )
//            tvPrice.text = getString(R.string.s_egp, formatPrice(data.sellPrice)) + "\n" + getRentPrice(data.rentDuration, data.rentPrice)
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

            initPropertyFeaturesRv(data.propertyFeatures)

            getYoutubeUrlId(data.videoUrl)?.let {
                setupVideo(it)
            }

            tvRatings.text = getString(R.string.s_ratings, data.comments.size.toString())

            initCommentsRv(data.comments)
            initSimilarPropertiesRv(data.similarProperties)
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

    private fun setupVideo(videoId: String) {
        binding.apply {
            lifecycle.addObserver(FAboutUsYoutubeView)
            FAboutUsYoutubeView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    onVideoId(youTubePlayer, videoId)
                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.play()
                }
            })
        }
    }

    private fun initPropertyFeaturesRv(data: List<String>) {
        if (data.isNotEmpty()) {
            binding.propertyFeaturesLayout.root.visibility = View.VISIBLE

            binding.propertyFeaturesLayout.rv.adapter = rvAmenitiesAdapter
            rvAmenitiesAdapter.submitList(data)
        }else{
            binding.propertyFeaturesLayout.root.visibility = View.GONE
        }
    }

    private fun initCommentsRv(data: List<RatingCommentsModel>) {
        binding.rvRatingComments.adapter = rvRatingAdapter
        rvRatingAdapter.submitList(data)
    }

    private fun initSimilarPropertiesRv(data: List<PropertyModel>) {
        binding.rvSimilarProperties.adapter = rvSimilarPropertiesAdapter
        rvSimilarPropertiesAdapter.submitList(data)
    }

    private fun getRentPrice(duration: String, price: Double): String {
        return when (duration) {
            RentDuration.DAILY.key -> {
                requireContext().getString(R.string.s_daily_price, formatPrice(price))
            }

            RentDuration.MONTHLY.key -> {
                requireContext().getString(R.string.s_monthly_price, formatPrice(price))
            }

            RentDuration.THREE_MONTHS.key -> {
                requireContext().getString(R.string.s_3_months_price, formatPrice(price))
            }

            RentDuration.SIX_MONTHS.key -> {
                requireContext().getString(R.string.s_6_months_price, formatPrice(price))
            }

            RentDuration.NINE_MONTHS.key -> {
                requireContext().getString(R.string.s_9_months_price, formatPrice(price))
            }

            RentDuration.YEARLY.key -> {
                requireContext().getString(R.string.s_yearly_price, formatPrice(price))
            }

            else -> ""
        }
    }
}