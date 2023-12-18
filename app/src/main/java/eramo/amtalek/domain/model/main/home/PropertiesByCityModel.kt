package eramo.amtalek.domain.model.main.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertiesByCityModel(
    val id: Int,
    val imageUrl: String,
    val cityName: String,
    val forSellCount: Int,
    val forRentCount: Int
) : Parcelable