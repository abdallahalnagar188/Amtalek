package eramo.amtalek.presentation.ui.main.home.details.properties

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import eramo.amtalek.data.remote.dto.brokersDetails.Data
import eramo.amtalek.databinding.FragmentPropertyDetailsBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.ChartModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvAmenitiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsViewModel
import eramo.amtalek.util.ROLLING_TEXT_ANIMATION_DURATION
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import eramo.amtalek.util.getYoutubeUrlId
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject


@AndroidEntryPoint
class PropertyDetailsFragment : BindingFragment<FragmentPropertyDetailsBinding>(),RvSimilarPropertiesAdapter.OnItemClickListener,
    RvSimilarPropertiesAdapter.OnFavClickListener{

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertyDetailsBinding::inflate

    private val viewModel: PropertyDetailsViewModel by viewModels()

    private val args by navArgs<PropertyDetailsFragmentArgs>()
    private val LoginData = MutableStateFlow<UiState<Data>>(UiState.Empty())

    private val propertyListingNumber get() = args.propertyId
    lateinit var propertyId:String
    private lateinit var vendorId:String

    @Inject
    lateinit var rvAmenitiesAdapter: RvAmenitiesAdapter

    @Inject
    lateinit var rvRatingAdapter: RvRatingAdapter

    @Inject
    lateinit var rvSimilarPropertiesAdapter: RvSimilarPropertiesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkActorType()
        setupViews()
        requestData()
        fetchData()
        clickListeners()
        setupToggle()
    }

    private fun setupToggle() {
        binding.toggleGroup.addOnButtonCheckedListener{ _, checkedId, isChecked ->
            if (isChecked){
                when(checkedId){
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
                    R.id.rate_btn-> {
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
        binding.offerBtn.isChecked =true
    }


    private fun clickListeners() {
        binding.btnSendRate.setOnClickListener(){
            if (validRateForm()){
                binding.apply {
                    val name = etName.text.toString()
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
        binding.btnOfferSend.setOnClickListener(){
            if (validOfferForm()){
                binding.apply {
                    val name = etOfferName.text.toString()
                    val email = etOfferMail.text.toString()
                    val phone = etOfferPhone.text.toString()
                    val offer = etOfferValue.text.toString()
                    var offerType = ""
                    if (autoCompleteOfferType.text.toString()== getString(R.string.rent)){
                        offerType = "for_rent"
                    }else if (autoCompleteOfferType.text.toString() == getString(R.string.buy)){
                        offerType = "for_sale"

                    }else{
                        Toast.makeText(requireContext(), getString(R.string.choose_a_valid_offer_type), Toast.LENGTH_SHORT).show()
                    }
                    val vendorId = vendorId
                    val propertyId = propertyId

                    viewModel.sendPropertyOffer(
                        name = name,
                        email = email,
                        phone = phone,
                        offer = offer,
                        offerType = offerType,
                        vendorId = vendorId,
                        propertyId = propertyId,
                    )

                }
            }
        }
        binding.btnMessageSend.setOnClickListener(){
            if (validMessageForm()){
                val name = binding.etMessageName.text.toString()
                val email = binding.etMessageMail.text.toString()
                val phone = binding.etMessagePhone.text.toString()
                val message = binding.etMessageValue.text.toString()
                val vendorId = vendorId
                val propertyId = propertyId
                viewModel.sendMessageToPropertyOwner(
                    name = name,
                    email = email,
                    phone = phone,
                    message = message,
                    vendorId = vendorId,
                    propertyId = propertyId
                )

            }
        }
    }

    private fun checkActorType() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                LoginData.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            binding.shimmerLayoutShareView.apply {
                                if (state.data?.broker_type == "broker" && state.data?.has_package == "yes") {
                                    visibility = View.VISIBLE
                                } else {
                                    visibility = View.GONE
                                }
                            }

                        }

                        is UiState.Empty -> {
                            Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {

                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        setupToolbar()

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
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
        if (UserUtil.getUserType()=="broker"){
            binding.ivFavourite.visibility = View.GONE
        }else{
            binding.ivFavourite.visibility = View.VISIBLE
        }
    }

    private fun requestData() {
        viewModel.getPropertyDetails(propertyListingNumber)
    }

    private fun fetchData() {
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
                            loadOfferTypes(state.data.forWhat!!)
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
    private fun assignData(data: PropertyDetailsModel) {
        try {
            binding.apply {
                var isFav = data.isFavourite
                ivFavourite.setOnClickListener {
                    viewModel.addOrRemoveFav(data.id)
                    if (isFav =="0") {ivFavourite.setImageResource(R.drawable.ic_heart_fill)
                        isFav = "1"
                    }
                    else {ivFavourite.setImageResource(R.drawable.ic_heart)
                        isFav ="0"
                    }
                }
                if (isFav == "1") {
                    ivFavourite.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFavourite.setImageResource(R.drawable.ic_heart)
                }
                propertyId = data.id.toString()
                vendorId = data.brokerId.toString()
                setupImageSliderTop(data.sliderImages)
                 checkRentDuration(data.rentDuration)
                when (data.forWhat) {
                    "for_rent" -> {
                        setRentPriceValue(formatPrice(data.rentPrice))
                        tvCurrencyRent.text = " "+ data.currency
                        tvOr.isVisible =false
                        tvPrice.isVisible = false
                        tvPriceAnimation.isVisible = false
                    }
                    "for_sale" -> {
                        setPriceValue(formatPrice(data.sellPrice))
                        tvCurrency.text = " "+ data.currency
                        tvOr.isVisible =false
                        tvPriceRent.isVisible = false
                        tvPriceAnimationRent.isVisible = false
                    }
                    "for_both" -> {
                        setPriceValue(formatPrice(data.sellPrice))
                        setRentPriceValue(formatPrice(data.rentPrice))
                        tvCurrency.text = " "+ data.currency
                        tvCurrencyRent.text = " "+ data.currency
                        tvOr.isVisible =true
                    }
                }


//                tvCurrency.text = " " + getRentPrice(requireContext(), data.rentDuration, data.currency) + " "

                tvTitle.text = data.title
                tvLocation.text = data.location
                tvDate.text = data.datePosted
                // header layout
                tvFinishing.text = data.finishing
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

                if (data.mapUrl.isNullOrEmpty()){
                    webView.visibility = View.GONE
                    mymapCardView.visibility = View.GONE
                }else{
                    mapSetup(data.mapUrl)
                }

                propertyDetailsLayout.tvPropertyCodeValue.text = data.propertyCode
                propertyDetailsLayout.tvTypeValue.text = data.propertyType
                propertyDetailsLayout.tvAreaValue.text = getString(R.string.s_meter_square, formatNumber(data.area))
                propertyDetailsLayout.tvBedroomsValue.text = data.bedroomsCount.toString()
                propertyDetailsLayout.tvBathroomValue.text = data.bathroomsCount.toString()
                propertyDetailsLayout.tvFinishingValue.text = data.finishing
                propertyDetailsLayout.tvFloorsValue.text = data.floors.joinToString(", ")
                propertyDetailsLayout.tvFloorValue.text = data.landType

                tvDescriptionValue.text = data.description

                initPropertyFeaturesRv(data.propertyAmenities)

                if (data.videoUrl.isNullOrEmpty()){
                    youtubePlayerView.visibility = View.GONE
                }else{
                    getYoutubeUrlId(data.videoUrl)?.let {
                        setupVideo(it)
                    }
                }

                tvRatings.text = getString(R.string.s_ratings, data.comments.size.toString())

                initCommentsRv(data.comments)
                initSimilarPropertiesRv(data.similarProperties)

                if (data.sold){
                    soldCardView.visibility = View.VISIBLE
                }else{
                    soldCardView.visibility = View.GONE
                }
                if (data.calcRoi == "yes"){
                    roiLayout.tvRoiValue.text = data.roi
                }else if (data.calcRoi == "no"){
                    roiLayout.root.visibility = View.GONE
                }

            }
            dismissShimmerEffect()

        } catch (e: Exception) {
            dismissShimmerEffect()
            showToast(getString(R.string.invalid_data_parsing))
        }
    }

    private fun checkRentDuration(rentDuration: String) {
        binding.apply {
            var result:String = ""
            when (rentDuration) {
                "daily" -> {
                    result ="${getString(R.string.per)} ${getString(R.string.daily)}"
                    tvRentDuration.text = result
                }
                "monthly" -> {
                    result ="${getString(R.string.per)} ${getString(R.string.monthly)}"
                    tvRentDuration.text = result
                }
                "3_months" -> {
                    result ="${getString(R.string.per)} ${getString(R.string._3_months)}"
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
        val video = data
        video?.let {
            binding.webView.settings.javaScriptEnabled =true
            binding.webView.settings.loadWithOverviewMode = true
            binding.webView.settings.useWideViewPort = true
            binding.webView.settings.setSupportZoom(false)
            binding.webView.settings.builtInZoomControls = false
            binding.webView.settings.displayZoomControls = false
            binding.webView.loadData(video,"text/html","utf-8")

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
        rvSimilarPropertiesAdapter.setListener(this,this)
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
        for (item in chartList){
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
    private fun validOfferForm():Boolean{
        var isValid = true
        binding.apply {
            if (etOfferName.text.toString().isEmpty()){
                isValid = false
                etOfferName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etOfferMail.text.toString())){
                isValid = false
                etOfferMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etOfferPhone.text.toString().isEmpty()){
                etOfferPhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etOfferValue.text.toString().isEmpty()){
                etOfferValue.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (autoCompleteOfferType.text.toString().isEmpty()) {
                autoCompleteOfferType.error = getString(R.string.please_provide_your_offer_type)
                isValid = false
            }
            binding.autoCompleteOfferType.setOnItemClickListener { parent, view, position, id ->
                autoCompleteOfferType.error = null
            }
        }
        return isValid
    }
    private fun validMessageForm():Boolean{
        var isValid = true
        binding.apply {
            if (etMessageName.text.toString().isEmpty()){
                isValid = false
                etMessageName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etMessageMail.text.toString())){
                isValid = false
                etMessageMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etMessagePhone.text.toString().isEmpty()){
                etMessagePhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etMessageValue.text.toString().isEmpty()){
                etMessageValue.error = getString(R.string.please_enter_a_message)
                isValid = false
            }
        }
        return isValid
    }
    private fun validRateForm():Boolean{
        var isValid = true
        binding.apply {
            if (etName.text.toString().isEmpty()){
                isValid = false
                etName.error = getString(R.string.please_enter_a_name)
            }
            if (!isValidEmail(etMail.text.toString())){
                isValid = false
                etMail.error = getString(R.string.please_enter_a_mail)
            }
            if (etPhone.text.toString().isEmpty()){
                etPhone.error = getString(R.string.please_enter_a_phone_number)
                isValid = false
            }
            if (etYourRate.text.toString().isEmpty()){
                etYourRate.error = getString(R.string.please_enter_a_message)
                isValid = false
            }
        }
        return isValid
    }


    private fun loadOfferTypes(propertyType: String) {
        Log.e("Type", propertyType, )
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

            shareView.root.visibility = View.GONE
            shimmerLayoutShareView.visibility = View.VISIBLE
        }
    }

    private fun dismissShimmerEffect() {
        binding.apply {
            shimmerLayout.stopShimmer()

            viewLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE

            shareView.root.visibility = View.VISIBLE
            shimmerLayoutShareView.visibility = View.GONE
        }
    }

    override fun onFeaturedRealEstateClick(model: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel) {
        findNavController().navigate(R.id.propertyDetailsFragment,
            PropertyDetailsFragmentArgs(model.listingNumber).toBundle()
        )
    }

    override fun onFavClick(model: PropertyModel) {
        viewModel.addOrRemoveFav(model.id)
    }
}