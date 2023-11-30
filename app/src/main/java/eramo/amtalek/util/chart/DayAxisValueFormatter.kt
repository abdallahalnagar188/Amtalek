package eramo.amtalek.util.chart

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.formatter.ValueFormatter
import eramo.amtalek.domain.model.property.ChartModel

class DayAxisValueFormatter(cart: BarLineChartBase<*>, data: List<ChartModel>) : ValueFormatter() {

    val data = data
    override fun getFormattedValue(value: Float): String {
        return data[value.toInt() - 1].day
    }

}