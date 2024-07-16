package eramo.amtalek.domain.model.project

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmenityModel(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
) : Parcelable
