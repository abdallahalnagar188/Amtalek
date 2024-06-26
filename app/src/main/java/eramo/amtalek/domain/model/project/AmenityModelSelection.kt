package eramo.amtalek.domain.model.project

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmenityModelSelection(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false
) : Parcelable
