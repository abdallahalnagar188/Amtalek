package eramo.amtalek.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import eramo.amtalek.R
import eramo.amtalek.presentation.ui.MainActivity

object DataCharts {

    fun drawLineChart(context: Context?, xAxis_value: IntArray, yAxis_value: IntArray): LineData {
        // y axis values
        val yEntries: ArrayList<Entry> = ArrayList()
        for (i in xAxis_value.indices)
            yEntries.add(Entry(xAxis_value[i].toFloat(), yAxis_value[i].toFloat()))

        //collect values in a set
        val lineDataSet = LineDataSet(yEntries, null)
        lineDataSet.form = Legend.LegendForm.NONE
        lineDataSet.fillAlpha = 120
//        lineDataSet.fillDrawable = ContextCompat.getDrawable(context, R.drawable.shape_line_chart)
        lineDataSet.setDrawFilled(true)
        lineDataSet.lineWidth = 1.5f
        lineDataSet.valueTextSize = 12f
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.circleRadius = 2f
//        lineDataSet.setCircleColor(MainActivity.colorOnSecondary)
//        lineDataSet.valueTextColor = MainActivity.colorOnSecondary
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        //list of data sets which everyone makes a line
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)

        //assign all data sets to line chart
        return LineData(dataSets)
    }

    class AxisFormat : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toInt().toString()
        }
    }

}