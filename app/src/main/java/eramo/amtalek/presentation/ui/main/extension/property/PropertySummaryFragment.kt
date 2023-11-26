package eramo.amtalek.presentation.ui.main.extension.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPropertySummaryBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.DataCharts
import eramo.amtalek.util.DataCharts.drawLineChart


@AndroidEntryPoint
class PropertySummaryFragment : BindingFragment<FragmentPropertySummaryBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPropertySummaryBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVideo()
        initLineChart()
        binding.apply {

        }
    }

    private fun setupVideo() {
        val videoId = "zo40BGfu5Gg"
        binding.apply {
            lifecycle.addObserver(FAboutUsYoutubeView)
            FAboutUsYoutubeView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    onVideoId(youTubePlayer, videoId)
                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.pause()
                }
            })
        }
    }

    private fun initLineChart() {
        // fixed line chart for now
        // fixed line chart for now
        val xAxisValue = IntArray(12)
        val yAxisValue = intArrayOf(10, 15, 12, 18, 14, 16, 20, 18, 14, 16, 20, 22)


        for (i in xAxisValue.indices) xAxisValue[i] = i

        binding.lineChart.apply {
            data = drawLineChart(context, xAxisValue, yAxisValue)

            isDragEnabled = true
            setScaleEnabled(true)
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            isAutoScaleMinMaxEnabled = true
            animateY(1000)
            description = null
            fitScreen()
            zoomIn()
            zoomOut()
            setTouchEnabled(true)
            setPinchZoom(true)
            setVisibleXRangeMaximum(7f)

            // x axis edit

            // x axis edit
            val xValues = arrayOf(
                "12 AM",
                "2 AM",
                "4 AM",
                "6 AM",
                "8 AM",
                "10 AM",
                "12 PM",
                "2 PM",
                "4 PM",
                "6 PM",
                "8 PM",
                "10 PM"
            )

            val xAxis: XAxis = xAxis
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark)
            xAxis.granularity = 1f
            xAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = IndexAxisValueFormatter(xValues)

            // y axis edit

            // y axis edit
            val yAxis: YAxis = axisLeft
            yAxis.axisMinimum = 0f
            yAxis.labelCount = 5
            yAxis.axisMaximum = yAxis.axisMaximum + 10
            yAxis.textColor = ContextCompat.getColor(requireContext(), R.color.amtalek_blue_dark)
            yAxis.setDrawLimitLinesBehindData(true)
            yAxis.setDrawGridLines(false)
            yAxis.valueFormatter = DataCharts.AxisFormat()
        }
    }

}