package eramo.amtalek.presentation.ui.main.home.details.properties

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
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.strategy.Direction
import com.yy.mobile.rollingtextview.strategy.Strategy
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertyDetailsSellBinding
import eramo.amtalek.domain.model.property.ChartModel
import eramo.amtalek.domain.model.property.PropertyDetailsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvAmenitiesAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.navbottom.extension.PropertyDetailsSellViewModel
import eramo.amtalek.util.ROLLING_TEXT_ANIMATION_DURATION
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.chart.DayAxisValueFormatter
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import eramo.amtalek.util.getYoutubeUrlId
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.delay
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
    lateinit var rvAmenitiesAdapter: RvAmenitiesAdapter

    @Inject
    lateinit var rvSimilarPropertiesAdapter: RvSimilarPropertiesAdapter

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

        requestData()
        fetchData()


    }

    private fun setPriceValue(number: String) {
        binding.tvPriceAnimation.apply {
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
                delayForEnterAnimation()
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

    private fun assignData(data: PropertyDetailsModel) {
        try {
            binding.apply {

                setupImageSliderTop(data.sliderImages)

                setPriceValue(formatPrice(data.sellPrice))
                tvCurrency.text = " " + data.currency + " "

                tvTitle.text = data.title
                tvLocation.text = data.location
                tvDate.text = data.datePosted

                if (data.videoUrl.isNullOrEmpty()) {
                    tvVideo.visibility = View.GONE
                    youtubePlayerView.visibility = View.GONE
                } else {
                    getYoutubeUrlId(data.videoUrl)?.let {
                        setupVideo(it)
                    }
                }

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

                propertyDetailsLayout.tvFinishingValue.text = data.finishing
                propertyDetailsLayout.tvFloorsValue.text = data.floors.joinToString(", ")
                propertyDetailsLayout.tvFloorValue.text = data.landType

                tvDescriptionValue.text = data.description

//                initPropertyFeaturesRv(data.propertyFeatures)

//                getYoutubeUrlId(data.videoUrl)?.let {
//                    setupVideo(it)
//                }

//            val cartList = data.chartList.toMutableList()
//
//            cartList.add(
//                0,
//                (ChartModel(
//                    -1, 0, ""
//                ))
//            )
                if (data.chartList.isNotEmpty()) {
                    binding.tvViewsChart.visibility = View.VISIBLE
                    binding.chart.visibility = View.VISIBLE
                    setupChartView(getChartList(data))
                } else {
                    binding.tvViewsChart.visibility = View.GONE
                    binding.chart.visibility = View.GONE
                }

                //            val fakeList = mutableListOf<ChartModel>()
//            fakeList.add(ChartModel(
//                1,4,"2222-22-22"
//            ))
//            fakeList.add(ChartModel(
//                2,6,"3333-33-33"
//            ))
//            fakeList.add(ChartModel(
//                3,5,"44"
//            ))
//            setupChartView(fakeList)

                tvRatings.text = getString(R.string.s_ratings, data.comments.size.toString())

                initCommentsRv(data.comments)
                initSimilarPropertiesRv(data.similarProperties)
            }

        } catch (e: Exception) {
            dismissShimmerEffect()
            showToast(getString(R.string.invalid_data_parsing))
        }
        dismissShimmerEffect()
    }

    private fun initPropertyFeaturesRv(data: List<String>) {
        binding.apply {
            if (data.isNotEmpty()) {
                propertyFeaturesLayout.root.visibility = View.VISIBLE

                val layoutManager = FlexboxLayoutManager(requireContext())
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.flexWrap = FlexWrap.WRAP

                propertyFeaturesLayout.rv.layoutManager = layoutManager
                propertyFeaturesLayout.rv.adapter = rvAmenitiesAdapter
//                rvAmenitiesAdapter.submitList(data)
            } else {
                binding.propertyFeaturesLayout.root.visibility = View.GONE
            }
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

    private fun initCommentsRv(data: List<RatingCommentsModel>) {
        binding.rvRatingComments.adapter = rvRatingAdapter
        rvRatingAdapter.submitList(data)
    }

    private fun initSimilarPropertiesRv(data: List<eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel>) {
        binding.rvSimilarProperties.adapter = rvSimilarPropertiesAdapter
        rvSimilarPropertiesAdapter.submitList(data)
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
                    -1,
                ))
            )
        }

        return cartList.takeLast(5)
    }

    private suspend fun delayForEnterAnimation() {
        showShimmerEffect()
        delay(450)
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