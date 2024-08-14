package eramo.amtalek.data.remote.dto.userDetials

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Amenity(
    val amenities: Int?,
    val created_at: String?,
    val id: Int?,
    val property_id: Int?,
    val updated_at: String?
): Parcelable