package eramo.amtalek.domain.model.main.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectModel(
//    val imageUrl: String,
//    val title: String,
//    val description: String,
//    val location: String,
//    val date: String,
//    val isFavourite: Int,
//    val brokerName: String,
//    val brokerLogoUrl: String

    val id: Int,
    val imageUrl: String,
    val isFavourite: Int,
    val title: String,
    val description: String,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String
) : Parcelable