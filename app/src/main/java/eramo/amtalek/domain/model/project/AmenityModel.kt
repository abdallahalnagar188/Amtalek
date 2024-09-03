package eramo.amtalek.domain.model.project

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmenityModel(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false,
    var image: String?
) : Parcelable

@Parcelize
data class AutocadModel(
    val id: Int,
    val src: String,
    val type: String
) : Parcelable
