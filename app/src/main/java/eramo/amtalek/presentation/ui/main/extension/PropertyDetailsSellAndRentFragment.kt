package eramo.amtalek.presentation.ui.main.extension

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.strategy.Direction
import com.yy.mobile.rollingtextview.strategy.Strategy
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsSellAndRentBinding
import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.domain.model.property.ChartModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvAmenitiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsSellAndRentViewModel
import eramo.amtalek.util.ROLLING_TEXT_ANIMATION_DURATION
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.chart.DayAxisValueFormatter
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
                            assignData(state.data!!)
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

                setupImageSliderTop(data.sliderImages)


                setSellPriceValue(formatPrice(data.sellPrice))
                tvSellCurrency.text = " " + data.currency + " "

                setRentPriceValue(formatPrice(data.sellPrice))
                tvRentCurrency.text = " " + getRentPrice(requireContext(), data.rentDuration, data.currency) + " "


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

                if (data.chartList.isNotEmpty()) {
                    binding.tvViewsChart.visibility = View.VISIBLE
                    binding.chart.visibility = View.VISIBLE
                    setupChartView(getChartList(data))
                } else {
                    binding.tvViewsChart.visibility = View.GONE
                    binding.chart.visibility = View.GONE
                }

                tvRatings.text = getString(R.string.s_ratings, data.comments.size.toString())

                initCommentsRv(data.comments)
                initSimilarPropertiesRv(data.similarProperties)
            }

            dismissShimmerEffect()
        } catch (e: Exception) {
            dismissShimmerEffect()
            showToast(getString(R.string.invalid_data_parsing))
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

    private fun setupVideo(videoId: String) {
        binding.apply {
            lifecycle.addObserver(youtubePlayerView)
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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
        } else {
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

    private fun setupChartView(chartList: List<ChartModel>) {
        binding.apply {
            val linevalues = ArrayList<Entry>()

//            linevalues.add(Entry(1f, 10F))
//            linevalues.add(Entry(2f, 80F))
//            linevalues.add(Entry(3f, 50F))
//            linevalues.add(Entry(4f, 20F))

            for (i in chartList) {
                linevalues.add(
                    Entry((chartList.indexOf(i) + 1).toFloat(), i.viewsCount.toFloat())
                )
            }

            val linedataset = LineDataSet(linevalues, null)

            // line
            linedataset.color = ContextCompat.getColor(requireContext(), R.color.yellow)
            linedataset.lineWidth = 2f

            // circle point
            linedataset.circleRadius = 4f
            linedataset.setCircleColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            linedataset.circleHoleColor = ContextCompat.getColor(requireContext(), R.color.yellow)
            linedataset.valueTextSize = 0F

            // chart design
            linedataset.setDrawFilled(false)
            linedataset.fillColor = ContextCompat.getColor(requireContext(), R.color.black)
            linedataset.mode = LineDataSet.Mode.CUBIC_BEZIER

            //We connect our data to the UI Screen
            val data = LineData(linedataset)
            chart.data = data
            chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            chart.animateXY(2000, 2000, Easing.EaseInCubic)
            chart.extraBottomOffset = 20f

            chart.xAxis.setDrawGridLines(false)

            // Customize other axis properties if needed
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.axisRight.isEnabled = false
            chart.legend.isEnabled = false

            // xAxis values
            val xAxisFormatter = DayAxisValueFormatter(chart, chartList)
            val xAxis = chart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f

            chart.xAxis.labelRotationAngle = -60f
            chart.xAxis.textSize = 9f
            chart.description.isEnabled = false
            chart.xAxis.valueFormatter = xAxisFormatter

            chart.setTouchEnabled(false)
        }
    }

    private fun getChartList(data: PropertyDetailsModel): List<ChartModel> {
        val cartList = data.chartList.toMutableList()

        if (cartList.size <= 1) {
            cartList.add(
                0,
                (ChartModel(
                    -1, 0, ""
                ))
            )
        }

        return cartList.takeLast(5)
    }

    private fun setSellPriceValue(number: String) {
        binding.tvSellPriceAnimation.apply {
            animationDuration = ROLLING_TEXT_ANIMATION_DURATION
            charStrategy = Strategy.SameDirectionAnimation(Direction.SCROLL_DOWN)
            addCharOrder(CharOrder.Number)
            setText(number)
        }
    }

    private fun setRentPriceValue(number: String) {
        binding.tvRentPriceAnimation.apply {
            animationDuration = ROLLING_TEXT_ANIMATION_DURATION
            charStrategy = Strategy.SameDirectionAnimation(Direction.SCROLL_DOWN)
            addCharOrder(CharOrder.Number)
            setText(number)
        }
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
}