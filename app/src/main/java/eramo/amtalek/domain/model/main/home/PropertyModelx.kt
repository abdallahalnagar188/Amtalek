package eramo.amtalek.domain.model.main.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PropertyModelx(
    val id :Int,
    val imageUrl: String,
    val type: String,
    val isFavourite: Int,
    val isFeatured: Int,
    val sellPrice: Double,
    val rentPrice: Double,
    val currency: String,
    val rentDuration: String,
    val title: String,
    val area: Int,
    val bathroomsCount: Int,
    val bedsCount: Int,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String
):Parcelable
