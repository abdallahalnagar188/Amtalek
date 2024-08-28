package eramo.amtalek.presentation.ui.main.home.details.properties

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.strategy.Direction
import com.yy.mobile.rollingtextview.strategy.Strategy
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsBinding
import eramo.amtalek.databinding.ItemSliderTopBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.home.slider.SliderModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.project.AutocadModel
import eramo.amtalek.domain.model.property.ChartModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvAmenitiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel

import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.ROLLING_TEXT_ANIMATION_DURATION
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import eramo.amtalek.util.getYoutubeUrlId
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import javax.inject.Inject


@AndroidEntryPoint
class PropertyDetailsFragment : BindingFragment<FragmentPropertyDetailsBinding>(),
    RvSimilarPropertiesAdapter.OnItemClickListener,
    RvSimilarPropertiesAdapter.OnFavClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsBinding::inflate


    private val viewModel: PropertyDetailsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val homeViewModel: HomeMyViewModel by viewModels()

    private val args by navArgs<PropertyDetailsFragmentArgs>()

    private val propertyListingNumber get() = args.propertyId
    lateinit var propertyId: String
    private lateinit var vendorId: String
    private lateinit var vendorType: String
    private lateinit var forWhat: String

    @Inject
    lateinit var rvAmenitiesAdapter: RvAmenitiesAdapter

    @Inject
    lateinit var rvRatingAdapter: RvRatingAdapter

    @Inject
    lateinit var rvSimilarPropertiesAdapter: RvSimilarPropertiesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // checkActorTypefirstime()
        viewModel.getPropertySlider()
        setupViews()
        requestData()
        fetchData()
        clickListeners()
        setupToggle()
    }


    private fun setupToggle() {
        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.offer_btn -> {
                        binding.apply {
                            offerBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            offerBtn.setTextColor(context?.getColor(R.color.white)!!)
                            messageBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            messageBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            rateBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            rateBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            messageLayoutRoot.visibility = View.GONE
                            offerLayoutRoot.visibility = View.VISIBLE
                            rateUsLayoutRoot.visibility = View.GONE


                        }
                    }

                    R.id.message_btn -> {
                        binding.apply {
                            messageBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            messageBtn.setTextColor(context?.getColor(R.color.white)!!)
                            offerBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            offerBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            rateBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            rateBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            messageLayoutRoot.visibility = View.VISIBLE
                            rateUsLayoutRoot.visibility = View.GONE
                            offerLayoutRoot.visibility = View.GONE

                        }
                    }

                    R.id.rate_btn -> {
                        binding.apply {
                            rateBtn.setBackgroundColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            rateBtn.setTextColor(context?.getColor(R.color.white)!!)
                            offerBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            offerBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            messageBtn.setBackgroundColor(context?.getColor(R.color.white)!!)
                            messageBtn.setTextColor(context?.getColor(R.color.amtalek_blue_dark)!!)
                            messageLayoutRoot.visibility = View.GONE
                            offerLayoutRoot.visibility = View.GONE
                            rateUsLayoutRoot.visibility = View.VISIBLE

                        }
                    }
                }
            }
        }
        binding.offerBtn.isChecked = true
    }


    private fun clickListeners() {
        binding.etName.setText(UserUtil.getUserFirstName() + UserUtil.getUserLastName())
        binding.etMail.setText(UserUtil.getUserEmail())
        binding.etPhone.setText(UserUtil.getUserPhone())
        binding.btnSendRate.setOnClickListener() {
            if (validRateForm()) {
                binding.apply {
                    val name = etName.text.toString().trim()
                    val email = etMail.text.toString()
                    val phone = etPhone.text.toString()
                    val message = etYourRate.text.toString()
                    val stars = rateBar.rating.toInt()
                    viewModel.sendCommentOnProperty(
                        propertyId = propertyId,
                        name = name,
                        email = email,
                        phone = phone,
                        message = message,
                        stars = stars
                    )
                }

            }
        }
        binding.etOfferName.setText(UserUtil.getUserFirstName() + UserUtil.getUserLastName())
        binding.etOfferMail.setText(UserUtil.getUserEmail())
        binding.etOfferPhone.setText(UserUtil.getUserPhone())
        binding.btnOfferSend.setOnClickListener() {

            if (validOfferForm()) {
                binding.apply {
                    val name = etOfferName.text.toString().trim()
                    val email = etOfferMail.text.toString()
                    val phone = etOfferPhone.text.toString()
                    val offer = etOfferValue.text.toString()
//                    var offerType = ""
//                    if (autoCompleteOfferType.text.toString() == getString(R.string.rent)) {
//                        offerType = "for_rent"
//                    } else if (autoCompleteOfferType.text.toString() == getString(R.string.buy)) {
//                        offerType = "for_sale"
//
//                    } else {
//                        Toast.makeText(requireContext(), getString(R.string.choose_a_valid_offer_type), Toast.LENGTH_SHORT).show()
//                    }
                    // val vendorId = vendorId
                    val propertyId = propertyId
                    val forWhat = forWhat

                    viewModel.sendPropertyOffer(
                        name = name,
                        email = email,
                        phone = phone,
                        offer = offer,
                        offerType = forWhat,
                        vendorId = vendorId,
                        propertyId = propertyId,
                    )
                }
            }
        }

        binding.etMessageName.setText(getString(R.string.S_user_name, UserUtil.getUserFirstName(), UserUtil.getUserLastName()))
        binding.etMessageMail.setText(UserUtil.getUserEmail())
        binding.etMessagePhone.setText(UserUtil.getUserPhone())
        binding.btnMessageSend.setOnClickListener() {
            if (validMessageForm()) {
                val name = binding.etMessageName.text.toString().trim()
                val email = binding.etMessageMail.text.toString()
                val phone = binding.etMessagePhone.text.toString()
                val message = binding.etMessageValue.text.toString().trim()
                val vendorId = vendorId
                val propertyId = propertyId
                val vendorType = vendorType
                viewModel.sendMessageToPropertyOwner(
                    name = name,
                    email = email,
                    phone = phone,
                    message = message,
                    vendorId = vendorId,
                    propertyId = propertyId,
                    vendorType = vendorType
                )
            }
        }
    }

    private fun navigateToProfile(model: PropertyDetailsModel) {
        findNavController().navigate(
            R.id.brokersDetailsFragment,
            bundleOf("id" to model.brokerId),
            navOptionsAnimation()
        )


    }

    private fun fetchGetPropertySlider() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertySliderState.collect() { state ->
                    when (state) {

                        is UiState.Success -> {
                            val data = state.data
                            if (!data.isNullOrEmpty()) {
                                binding.adsSlider.visibility = View.VISIBLE
                                setupSliderBetween(parseBetweenCarouselSliderList(data))
                            } else {
                                binding.adsSlider.visibility = View.GONE

                            }
                        }

                        is UiState.Error -> {
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private lateinit var sliderModels: List<SliderModel>
    private lateinit var carouselItems: List<CarouselItem>

    private fun parseBetweenCarouselSliderList(data: List<SliderModel>?): ArrayList<CarouselItem> {
        val list = ArrayList<CarouselItem>()
        sliderModels = data ?: emptyList()

        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        for (i in sliderModels) {
            list.add(
                CarouselItem(
                    imageUrl = i.image,
                    headers = headers
                )
            )
        }
        carouselItems = list
        return list
    }

    private fun setupSliderBetween(dataState: ArrayList<CarouselItem>) {
        binding.apply {
            adsSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            adsSlider.setData(dataState)

            if (dataState.size == 1) {
                adsSlider.infiniteCarousel = false
                adsSlider.autoPlay = false
                adsSlider.imageScaleType = ImageView.ScaleType.CENTER_INSIDE
            } else {
                adsSlider.infiniteCarousel = true
                adsSlider.autoPlay = true
                adsSlider.imageScaleType = ImageView.ScaleType.CENTER_INSIDE
            }

            adsSlider.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemSliderTopBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
                    val currentBinding = binding as ItemSliderTopBinding
                    val sliderModel = sliderModels[position]

                    currentBinding.apply {
                        imageView13.setImage(item)
                        imageView13.scaleType = ImageView.ScaleType.CENTER_CROP

                        // Access the SliderModel variables here
                        val id = sliderModel.id
                        val imageUrl = sliderModel.image
                        val type = sliderModel.type
                        val inFrame = sliderModel.inFrame
                        val url = sliderModel.url

                        // Use these variables as needed
                        this.root.setOnClickListener {
                            if (url.isNotEmpty()) {
                                setupListeners(url)
                                homeViewModel.clickedOnAd(id.toString())
                            }
                        }

                        this@PropertyDetailsFragment.binding
                    }
                }
            }
        }
    }

    private fun setupListeners(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }

//    private fun handleContactAction(propertyId: Int, brokerId: Int, brokerType: String, transactionType: String) {
//        sharedViewModel.sendContactRequest(propertyId, brokerId, brokerType = brokerType, transactionType)
//    }
//
//
//    private fun checkActorTypefirstime() {
//        Log.e("state", UserUtil.getUserType())
//        Log.e("state", UserUtil.getHasPackage())
//        if (UserUtil.getUserType() == "broker" && UserUtil.getHasPackage() == "yes") {
//            binding.contactUs.root.visibility = View.VISIBLE
//        } else {
//            binding.contactUs.root.visibility = View.GONE
//        }
//    }
//
//    private fun checkActorType() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.loginData.collect {
//                    when (it) {
//                        is UiState.Success -> {
//                            Log.e("state", it.data?.brokerType!!)
//                            Log.e("state", it.data.hasPackage!!)
//                            if (UserUtil.getUserType() == "broker" && it.data.hasPackage == "") {
//
//                            }
////                            if (it.data.brokerType == "broker" && it.data.hasPackage == "yes") {
////                                binding.contactUs.root.visibility = View.VISIBLE
////                            } else if (it.data.brokerType == "user" && it.data.hasPackage == "no"){
////                                binding.contactUs.root.visibility = View.GONE
////                            }
//                        }
//
//                        is UiState.Empty -> {
//                            Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
//                        }
//
//                        is UiState.Error -> {
//                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                        }
//
//                        is UiState.Loading -> {
//                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//        }
//    }


    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        setupToolbar()
        binding.autoCompleteOfferType.visibility = View.GONE
    }

    private fun setPriceValue(number: String) {
        binding.tvPriceAnimation.apply {
            animationDuration = ROLLING_TEXT_ANIMATION_DURATION
            charStrategy = Strategy.SameDirectionAnimation(Direction.SCROLL_DOWN)
            addCharOrder(CharOrder.Number)
            setText(number)
        }
    }

    private fun setRentPriceValue(number: String) {
        binding.tvPriceAnimationRent.apply {
            animationDuration = ROLLING_TEXT_ANIMATION_DURATION
            charStrategy = Strategy.SameDirectionAnimation(Direction.SCROLL_DOWN)
            addCharOrder(CharOrder.Number)
            setText(number)
        }
    }


    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {

            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
        if (UserUtil.getUserType() == "broker") {
            binding.ivFavourite.visibility = View.GONE
            binding.etMail.visibility = View.GONE
            binding.etPhone.visibility = View.GONE
            binding.etName.visibility = View.GONE
            binding.etOfferMail.visibility = View.GONE
            binding.etOfferName.visibility = View.GONE
            binding.etOfferPhone.visibility = View.GONE
            binding.etOfferValue.visibility = View.GONE
            binding.etMessageMail.visibility = View.GONE
            binding.etMessageName.visibility = View.GONE
            binding.etMessagePhone.visibility = View.GONE
            binding.etMessageValue.visibility = View.GONE
            binding.autoCompleteOfferType.visibility = View.GONE
            binding.btnOfferSend.visibility = View.GONE
            binding.btnMessageSend.visibility = View.GONE
            binding.btnSendRate.visibility = View.GONE
            binding.rateBar.visibility = View.GONE
            binding.etYourRate.visibility = View.GONE
            binding.tvRatings.visibility = View.GONE
            binding.ivArrowBroker.visibility = View.GONE
            binding.toggleGroup.visibility = View.GONE
            binding.tvRegisterWithUs.visibility = View.GONE
            binding.offerSpinner.visibility = View.GONE
            binding.toggleGroup.visibility = View.GONE
            binding.tilOfferValue.visibility = View.GONE
            binding.tilMessageValue.visibility = View.GONE
            binding.tilYourRate.visibility = View.GONE
            binding.tilMessageName.visibility = View.GONE
            binding.tilMessageMail.visibility = View.GONE
            binding.tilMessagePhone.visibility = View.GONE
            binding.tilOfferName.visibility = View.GONE
            binding.tilOfferMail.visibility = View.GONE
            binding.tilOfferPhone.visibility = View.GONE


        } else {
            binding.ivFavourite.visibility = View.VISIBLE
        }

    }

    private fun requestData() {
        viewModel.getPropertyDetails(propertyListingNumber)
    }

    private fun fetchData() {

        // checkActorTypefirstime()
        //fetchActorType()
        fetchGetPropertySlider()
        fetchPropertyDetailsState()
        fetchSendCommentOnPropertyState()
        fetchSendPropertyOfferState()
        fetchSubmitMessageToPropertyOwnerState()
    }

    private fun fetchSendPropertyOfferState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delayForEnterAnimation()
                viewModel.sendPropertyOfferState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            dismissShimmerEffect()
                            showToast(getString(R.string.offer_sent_successfully))
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(getString(R.string.sending_offer_before))
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun fetchSubmitMessageToPropertyOwnerState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delayForEnterAnimation()
                viewModel.sendMessageToPropertyOwnerState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            dismissShimmerEffect()
                            showToast(state.data?.message!!)
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun fetchSendCommentOnPropertyState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delayForEnterAnimation()
                viewModel.sendCommentOnPropertyState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            dismissShimmerEffect()
                            showToast(getString(R.string.comment_added_successfully))
                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun fetchPropertyDetailsState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delayForEnterAnimation()
                viewModel.propertyDetailsState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            assignData(state.data!!)
                            if (state.data.forWhat == "for_both") {
                                binding.offerSpinner.visibility = View.VISIBLE
                                binding.autoCompleteOfferType.visibility = View.VISIBLE
                                loadOfferTypes(state.data.forWhat!!)

                            }
                            binding.ivShare.setOnClickListener {
                                shareContent(
                                    listingNumber = state.data.listingNumber,
                                    title = state.data.title.toString()
                                )
                            }
                            propertyId = state.data.id.toString()
                            vendorId = state.data.brokerId.toString()
                            vendorType = state.data.vendorType
                            forWhat = state.data.forWhat
                            handleSuccessState(state.data)

                        }

                        is UiState.Error -> {
                            dismissShimmerEffect()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            showShimmerEffect()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun shareContent(title: String, listingNumber: String) {

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, if (LocalUtil.isEnglish()) {
                    "${getString(R.string.i_found_this_property_on_amtalek_com)}\nhttps://amtalek.com/properties/en/${listingNumber}/${title}"
                } else {
                    "${getString(R.string.i_found_this_property_on_amtalek_com)}\n https://amtalek.com/properties/${listingNumber}/${title}"
                }
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun assignData(model: PropertyDetailsModel) {
        try {
            binding.apply {
                var isFav = model.isFavourite
                ivFavourite.setOnClickListener {
                    viewModel.addOrRemoveFav(model.id)
                    if (UserUtil.isUserLogin()) {
                        if (isFav == "0") {
                            ivFavourite.setImageResource(R.drawable.ic_heart_fill)
                            isFav = "1"
                        } else {
                            ivFavourite.setImageResource(R.drawable.ic_heart)
                            isFav = "0"
                        }
                    } else {
                        findNavController().navigate(R.id.loginDialog)
                    }
                }

                if (isFav == "1") {
                    ivFavourite.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFavourite.setImageResource(R.drawable.ic_heart)
                }


                propertyId = model.id.toString()
                vendorId = model.brokerId.toString()
                setupImageSliderTop(model.sliderImages)
                checkRentDuration(model.rentDuration)
                Log.e("state", model.mapUrl)
                when (model.forWhat) {
                    "for_rent" -> {
                        setRentPriceValue(formatPrice(model.rentPrice))
                        tvCurrencyRent.text = " " + model.currency
                        tvOr.isVisible = false
                        tvPrice.isVisible = false
                        tvPriceAnimation.isVisible = false
                    }

                    "for_sale" -> {
                        setPriceValue(formatPrice(model.sellPrice))
                        tvCurrency.text = " " + model.currency
                        tvOr.isVisible = false
                        tvPriceRent.isVisible = false
                        tvPriceAnimationRent.isVisible = false
                    }

                    "for_both" -> {
                        setPriceValue(formatPrice(model.sellPrice))
                        setRentPriceValue(formatPrice(model.rentPrice))
                        tvCurrency.text = " " + model.currency
                        tvCurrencyRent.text = " " + model.currency
                        tvOr.isVisible = true
                    }
                }

                if (UserUtil.getUserId() == model.brokerId.toString()) {
                    btnSendRate.visibility = View.GONE
                    btnMessageSend.visibility = View.GONE
                    btnOfferSend.visibility = View.GONE
                    etYourRate.visibility = View.GONE
                    rateBar.visibility = View.GONE
                    tvRatings.visibility = View.GONE
                    etMail.visibility = View.GONE
                    etPhone.visibility = View.GONE
                    etName.visibility = View.GONE
                    offerSpinner.visibility = View.GONE
                    tilOfferValue.visibility = View.GONE
                    tilMessageValue.visibility = View.GONE
                    tilYourRate.visibility = View.GONE
                    tilMessageName.visibility = View.GONE
                    tilMessageMail.visibility = View.GONE
                    tilMessagePhone.visibility = View.GONE
                    tilOfferName.visibility = View.GONE
                    tilOfferMail.visibility = View.GONE
                    tilOfferPhone.visibility = View.GONE
                    toggleGroup.visibility = View.GONE
                    tvRegisterWithUs.visibility = View.GONE
                    tilMail.visibility = View.GONE
                    tilPhone.visibility = View.GONE
                    tilName.visibility = View.GONE
                    autoCompleteOfferType.visibility = View.GONE
                    tvVisitProfile.visibility = View.GONE
                    ivArrowBroker.visibility = View.GONE


                } else {
                    btnSendRate.visibility = View.VISIBLE
                    btnMessageSend.visibility = View.VISIBLE
                    btnOfferSend.visibility = View.VISIBLE
                    etYourRate.visibility = View.VISIBLE
                    rateBar.visibility = View.VISIBLE
                    tvRatings.visibility = View.VISIBLE
                    etMail.visibility = View.VISIBLE
                    etPhone.visibility = View.VISIBLE
                    etName.visibility = View.VISIBLE
                    offerSpinner.visibility = View.VISIBLE
                    tilOfferValue.visibility = View.VISIBLE
                    tilMessageValue.visibility = View.VISIBLE
                    tilYourRate.visibility = View.VISIBLE
                    tilMessageName.visibility = View.VISIBLE
                    tilMessageMail.visibility = View.VISIBLE
                    tilMessagePhone.visibility = View.VISIBLE
                    tilOfferName.visibility = View.VISIBLE
                    tilOfferMail.visibility = View.VISIBLE
                    tilOfferPhone.visibility = View.VISIBLE
                    toggleGroup.visibility = View.VISIBLE
                    tvRegisterWithUs.visibility = View.VISIBLE
                    tilMail.visibility = View.VISIBLE
                    tilPhone.visibility = View.VISIBLE
                    tilName.visibility = View.VISIBLE
                    autoCompleteOfferType.visibility = View.VISIBLE
                    tvVisitProfile.visibility = View.VISIBLE
                    ivArrowBroker.visibility = View.VISIBLE
                }

//                tvCurrency.text = " " + getRentPrice(requireContext(), data.rentDuration, data.currency) + " "

                tvTitle.text = model.title
                tvLocation.text = model.location
                tvDate.text = model.datePosted
                // header layout
                tvFinishing.text = model.finishing
                tvLocation2.text = model.areaLocation
                tvArea.text = getString(R.string.s_meter_square_me_n, formatNumber(model.area))
                tvBedrooms.text = getString(R.string.s_bedroom_count_n, model.bedroomsCount.toString())
                tvBathrooms.text = getString(R.string.s_bathroom_count_n, model.bathroomsCount.toString())


                offerSpinner.visibility = View.GONE
                tvUserName.text = model.brokerName
                tvUserId.text = model.brokerDescription

                Glide.with(requireContext())
                    .load(model.brokerImageUrl)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivUserImage)

                if (model.mapUrl.isNullOrEmpty()) {
                    webView.visibility = View.GONE
                    mymapCardView.visibility = View.GONE
                } else {
                    mapSetup(model.mapUrl)
                }
                if (model.videoUrl.isNullOrEmpty()) {
                    tvVideo.visibility = View.GONE
                    youtubePlayerView.visibility = View.GONE
                } else {
                    getYoutubeUrlId(model.videoUrl)?.let {
                        setupVideo(it)
                    }
                }

                if (model.autocad.isEmpty()) {
                    binding.autocadDrawingsSlider.visibility = View.GONE
                    binding.tvAutocadDrawings.visibility = View.GONE
                } else {
                    val autocadImages = handleAutocadImages(model.autocad)
                    setupAutocadImagesSlider(autocadImages)
                }

                propertyDetailsLayout.tvPropertyCodeValue.text = model.propertyCode
                propertyDetailsLayout.tvTypeValue.text = model.propertyType
                propertyDetailsLayout.tvAreaValue.text = getString(R.string.s_meter_square, formatNumber(model.area))
                propertyDetailsLayout.tvBedroomsValue.text = model.bedroomsCount.toString()
                propertyDetailsLayout.tvBathroomValue.text = model.bathroomsCount.toString()
                propertyDetailsLayout.tvFinishingValue.text = model.finishing
                propertyDetailsLayout.tvFloorsValue.text = model.floors.joinToString(", ")
                propertyDetailsLayout.tvFloorValue.text = model.landType

                val htmlContent = model.description ?: ""
                val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(htmlContent)
                }
                tvDescriptionValue.text = spannedText
                initPropertyFeaturesRv(model.propertyAmenities)

                if (model.videoUrl.isNullOrEmpty()) {
                    tvVideo.visibility = View.GONE
                    youtubePlayerView.visibility = View.GONE
                } else {
                    getYoutubeUrlId(model.videoUrl)?.let {
                        setupVideo(it)
                    }
                }
//                if (model.sold) {
//                    contactUs.root.visibility = View.VISIBLE
//                }else {
//                    contactUs.root.visibility = View.GONE
//                }
//                if (model.vendorType == "user") {
//                    tvVisitProfile.visibility = View.GONE
//                    ivArrowBroker.visibility = View.GONE
//                }
                tvVisitProfile.setOnClickListener {
                    if (model.vendorType == "broker")
                        navigateToProfile(model = model)
                    else if (model.vendorType == "user") {
                        val action = PropertyDetailsFragmentDirections.actionToUsersDetailsFragment(
                            model.brokerId.toString()
                        )
                        findNavController().navigate(action)
                        Log.e("id for user", model.id.toString())
                    }
                }


                tvRatings.text = getString(R.string.s_ratings, model.comments.size.toString())

                initCommentsRv(model.comments)
                initSimilarPropertiesRv(model.similarProperties)

                if (model.sold) {
                    soldCardView.visibility = View.VISIBLE
                } else {
                    soldCardView.visibility = View.GONE
                }
                if (model.calcRoi == "yes") {
                    roiLayout.tvRoiValue.text = model.roi
                } else if (model.calcRoi == "no") {
                    roiLayout.root.visibility = View.GONE
                }
                binding.contactUs.btnCall.setOnClickListener {
                    if (UserUtil.isUserLogin()) {
                        sharedViewModel.sendContactRequest(
                            propertyId = model.id,
                            brokerId = model.brokerId,
                            brokerType = model.vendorType,
                            transactionType = "call"
                        )

                        val phoneNumber = "+20${model.brokerPhone}"
                        makeCall(phoneNumber)
                    } else {
                        findNavController().navigate(R.id.loginDialog)
                    }
                }
                binding.contactUs.btnMessaging.setOnClickListener {
                    if (UserUtil.isUserLogin()) {
                        Log.e("id", model.brokerId.toString())
                        Log.e("type", model.vendorType)
                        sharedViewModel.sendContactRequest(
                            propertyId = model.id,
                            brokerId = model.brokerId,
                            brokerType = model.vendorType,
                            transactionType = "email"
                        )

                        val emailAddress = model.brokerEmail
                        sendEmail(emailAddress)
                    } else {
                        findNavController().navigate(R.id.loginDialog)
                    }

                }
                binding.contactUs.btnWhatsApp.setOnClickListener {
                    if (UserUtil.isUserLogin()) {

                        sharedViewModel.sendContactRequest(
                            propertyId = model.id,
                            brokerId = model.brokerId,
                            brokerType = model.vendorType,
                            transactionType = "meeting"
                        )
                        val phoneNumber =
                            model.brokerPhone
                        openWhatsApp(phoneNumber)
                    } else {

                        findNavController().navigate(R.id.loginDialog)
                    }

                }
            }
            dismissShimmerEffect()

        } catch (e: Exception) {
            dismissShimmerEffect()
            showToast(getString(R.string.invalid_data_parsing))
        }
    }

    private fun handleAutocadImages(autocad: List<AutocadModel?>?): List<CarouselItem> {
        val imageList = mutableListOf<CarouselItem>()
        for (image in autocad!!) {
            imageList.add(CarouselItem(imageUrl = image?.src))
        }
        return imageList
    }

    private fun makeCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(callIntent)
    }

    private fun openWhatsApp(phoneNumber: String) {
        val url = "https://api.whatsapp.com/send?phone=+20$phoneNumber"
        val sendIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        try {
            startActivity(sendIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "WhatsApp is not installed on your device", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendEmail(emailAddress: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
            putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            putExtra(Intent.EXTRA_TEXT, "Email body here")
        }
        startActivity(emailIntent)
    }

    private fun handleSuccessState(data: PropertyDetailsModel?) {
        if (data?.autocad.isNullOrEmpty()) {
            binding.autocadDrawingsSlider.visibility = View.GONE
            binding.tvAutocadDrawings.visibility = View.GONE
        } else {
            val autocadImages = handleAutocadImages(data?.autocad)
            setupAutocadImagesSlider(autocadImages)
        }
    }

    private fun checkRentDuration(rentDuration: String) {
        binding.apply {
            var result: String = ""
            when (rentDuration) {
                "daily" -> {
                    result = "${getString(R.string.per)} ${getString(R.string.daily)}"
                    tvRentDuration.text = result
                }

                "monthly" -> {
                    result = "${getString(R.string.per)} ${getString(R.string.monthly)}"
                    tvRentDuration.text = result
                }

                "3_months" -> {
                    result = "${getString(R.string.per)} ${getString(R.string._3_months)}"
                    tvRentDuration.text = result
                }

                "6_months" -> {
                    result = " ${getString(R.string.per)} ${getString(R.string._6_months)}"
                    tvRentDuration.text = result
                }

                "9_months" -> {
                    result = " ${getString(R.string.per)} ${getString(R.string._9_months)}"
                    tvRentDuration.text = result
                }

                "yearly" -> {
                    result = " ${getString(R.string.per)} ${getString(R.string.yearly)}"
                    tvRentDuration.text = result
                }
            }
        }
    }

    private fun setupImageSliderTop(imagesUrl: List<String>) {
        val list = mutableListOf<CarouselItem>()

        if (imagesUrl.isNotEmpty()) {
            for (i in imagesUrl) {
                list.add(
                    CarouselItem(
                        imageUrl = i
                    )
                )
            }
        } else {
            list.add(
                CarouselItem(
                    imageDrawable = R.drawable.ic_no_image

                )
            )
        }

        binding.apply {
            imageSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            imageSlider.setIndicator(imageSliderDots)
            imageSlider.imagePlaceholder = ContextCompat.getDrawable(requireContext(), R.drawable.ic_shimmer_background)
            imageSlider.setData(list)
        }
    }

    private fun mapSetup(data: String?) {
        data?.let {
            val adjustedData = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                    }
                    iframe, img, video {
                        max-width: 100%;
                        width: 100%;
                        height: 24rem;
                    }
                </style>
            </head>
            <body>
                $data
            </body>
            </html>
        """.trimIndent()

            binding.webView.settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
            }

            binding.webView.webChromeClient = WebChromeClient()
            binding.webView.loadData(adjustedData, "text/html", "utf-8")
        }


//        data?.let {
//            val webSettings: WebSettings = binding.webView.settings
//            webSettings.javaScriptEnabled = true
//            webSettings.loadWithOverviewMode = true
//            webSettings.useWideViewPort = true
//            webSettings.setSupportZoom(false)
//            webSettings.builtInZoomControls = false
//            webSettings.displayZoomControls = true
//            binding.webView.webViewClient = WebViewClient()
//            binding.webView.loadData(data, "text/html", "utf-8")
//        }
    }

    private fun setupAutocadImagesSlider(data: List<CarouselItem>) {
        binding.apply {
            autocadDrawingsSlider.registerLifecycle(viewLifecycleOwner.lifecycle)
            autocadDrawingsSlider.setData(data)
        }
    }

    private fun setupVideo(videoId: String) {
        binding.apply {
            lifecycle.addObserver(youtubePlayerView)
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    onVideoId(youTubePlayer, videoId)
                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.pause()
//                    youTubePlayer.play()
                }
            })
        }
    }

    private fun initPropertyFeaturesRv(data: List<AmenityModel>) {
        binding.apply {
            if (data.isNotEmpty()) {
                propertyFeaturesLayout.root.visibility = View.VISIBLE

//                val layoutManager = FlexboxLayoutManager(requireContext())
//                layoutManager.flexDirection = FlexDirection.ROW
//                layoutManager.flexWrap = FlexWrap.WRAP

//                propertyFeaturesLayout.rv.layoutManager = layoutManager
                propertyFeaturesLayout.rv.adapter = rvAmenitiesAdapter
                rvAmenitiesAdapter.submitList(data)
            } else {
                propertyFeaturesLayout.root.visibility = View.GONE
            }
        }
    }

    private fun initCommentsRv(data: List<RatingCommentsModel>) {
        binding.rvRatingComments.adapter = rvRatingAdapter
        rvRatingAdapter.submitList(data)
    }

    private fun initSimilarPropertiesRv(data: List<PropertyModel>) {
        binding.rvSimilarProperties.adapter = rvSimilarPropertiesAdapter
        rvSimilarPropertiesAdapter.setListener(this, this)
        rvSimilarPropertiesAdapter.submitList(data)
    }

    private fun getRentPrice(context: Context, duration: String, currency: String): String {
        return when (duration) {
            RentDuration.DAILY.key -> {
                context.getString(R.string.s_daily_notation, currency)
            }

            RentDuration.MONTHLY.key -> {
                context.getString(R.string.s_monthly_notation, currency)
            }

            RentDuration.THREE_MONTHS.key -> {
                context.getString(R.string.s_3_months_notation, currency)
            }

            RentDuration.SIX_MONTHS.key -> {
                context.getString(R.string.s_6_months_notation, currency)
            }

            RentDuration.NINE_MONTHS.key -> {
                context.getString(R.string.s_9_months_notation, currency)
            }

            RentDuration.YEARLY.key -> {
                context.getString(R.string.s_yearly_notation, currency)
            }

            else -> ""
        }
    }

//    private fun setupChartView(chartList: List<ChartModel>) {
//        binding.apply {
//            val linevalues = ArrayList<Entry>()
////            linevalues.add(Entry(1f, 10F))
////            linevalues.add(Entry(2f, 80F))
////            linevalues.add(Entry(3f, 50F))
////            linevalues.add(Entry(4f, 20F))
//
//            for (i in chartList) {
//                linevalues.add(
//                    Entry((chartList.indexOf(i) + 1).toFloat(), i.viewsCount.toFloat())
//                )
//            }
//
//            val linedataset = LineDataSet(linevalues, null)
//
//            // line
//            linedataset.color = ContextCompat.getColor(requireContext(), R.color.yellow)
//            linedataset.lineWidth = 2f
//
//            // circle point
//            linedataset.circleRadius = 4f
//            linedataset.setCircleColor(ContextCompat.getColor(requireContext(), R.color.yellow))
//            linedataset.circleHoleColor = ContextCompat.getColor(requireContext(), R.color.yellow)
//            linedataset.valueTextSize = 0F
//
//            // chart design
//            linedataset.setDrawFilled(false)
//            linedataset.fillColor = ContextCompat.getColor(requireContext(), R.color.black)
//            linedataset.mode = LineDataSet.Mode.CUBIC_BEZIER
//
//            //We connect our data to the UI Screen
//            val data = LineData(linedataset)
//            chart.data = data
//            chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
//            chart.animateXY(2000, 2000, Easing.EaseInCubic)
//            chart.extraBottomOffset = 20f
//
//            chart.xAxis.setDrawGridLines(false)
//
//
//
//            val yAxis = chart.axisLeft
//
//            // Customize other axis properties if needed
//            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//            chart.axisRight.isEnabled = false
//            chart.legend.isEnabled = false
//
//            // xAxis values
//            val xAxisFormatter = DayAxisValueFormatter(chart, chartList)
//            val xAxis = chart.xAxis
//            xAxis.position = XAxis.XAxisPosition.BOTTOM
//            xAxis.setDrawGridLines(false)
//            xAxis.granularity = 1f
//
//            chart.xAxis.labelRotationAngle = -60f
//            chart.xAxis.textSize = 9f
//            chart.description.isEnabled = false
//            chart.xAxis.valueFormatter = xAxisFormatter
//
//            chart.setTouchEnabled(false)
//        }
//    }

    private fun getChartList(data: PropertyDetailsModel): List<ChartModel> {
        val chartList = data.chartList.toMutableList()
        val list = mutableListOf<ChartModel>()
        for (item in chartList) {
            list.add(ChartModel(item.viewsCount))
        }

        return list
    }

    private suspend fun delayForEnterAnimation() {
        showShimmerEffect()
        delay(450)
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validOfferForm(): Boolean {
        var isValid = true
        binding.apply {
            if (etOfferName.text.toString().isEmpty()) {
                etOfferName.setHint(R.string.name)
                isValid = false
                etOfferName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etOfferMail.text.toString())) {
                isValid = false
                etOfferMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etOfferPhone.text.toString().isEmpty()) {
                etOfferPhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etOfferValue.text.toString().isEmpty()) {
                etOfferValue.error = getString(R.string.please_enter_a_offer_value)
                isValid = false
            }
//            if (autoCompleteOfferType.text.toString().isEmpty()) {
//                autoCompleteOfferType.error = getString(R.string.please_provide_your_offer_type)
//                isValid = false
//            }
//            binding.autoCompleteOfferType.setOnItemClickListener { parent, view, position, id ->
//                autoCompleteOfferType.error = null
//            }
        }
        return isValid
    }

    private fun validMessageForm(): Boolean {
        var isValid = true
        binding.apply {
            if (etMessageName.text.toString().isEmpty()) {

                isValid = false
                etMessageName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etMessageMail.text.toString())) {
                isValid = false
                etMessageMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etMessagePhone.text.toString().isEmpty()) {
                etMessagePhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etMessageValue.text.toString().isEmpty()) {
                etMessageValue.error = getString(R.string.please_enter_a_message)
                isValid = false
            }
        }
        return isValid
    }

    private fun validRateForm(): Boolean {
        var isValid = true
        binding.apply {
            if (etName.text.toString().isEmpty()) {
                isValid = false
                etName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etMail.text.toString())) {
                isValid = false
                etMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etPhone.text.toString().isEmpty()) {
                etPhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etYourRate.text.toString().isEmpty()) {
                etYourRate.error = getString(R.string.please_enter_a_message)
                isValid = false
            }
        }
        return isValid
    }


    private fun loadOfferTypes(propertyType: String) {
        Log.e("Type", propertyType)
        when (propertyType) {
            "for_both" -> {
                val offerTypes = resources.getStringArray(R.array.offer_type)
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, offerTypes)
                binding.autoCompleteOfferType.setAdapter(arrayAdapter)
            }

            "for_sale" -> {
                val offerTypes = resources.getStringArray(R.array.buy_array)
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, offerTypes)
                binding.autoCompleteOfferType.setAdapter(arrayAdapter)
            }

            "for_rent" -> {
                val offerTypes = resources.getStringArray(R.array.rent_array)
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, offerTypes)
                binding.autoCompleteOfferType.setAdapter(arrayAdapter)
            }
        }
        binding.autoCompleteOfferType.inputType = InputType.TYPE_NULL


    }

    private fun showShimmerEffect() {
        binding.apply {
            shimmerLayout.startShimmer()

            viewLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE


        }
    }

    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE

        }
    }

    override fun onFeaturedRealEstateClick(model: PropertyModel) {
        findNavController().navigate(
            R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onFavClick(model: PropertyModel) {
        if (UserUtil.isUserLogin()) {
            viewModel.addOrRemoveFav(model.id)
        } else {
            findNavController().navigate(R.id.loginDialog)
        }
    }
}