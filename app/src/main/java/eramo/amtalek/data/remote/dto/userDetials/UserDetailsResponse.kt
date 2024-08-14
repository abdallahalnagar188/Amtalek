package eramo.amtalek.data.remote.dto.userDetials

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class UserDetailsResponse(
    val `data`: List<Data>?,
    val message: String?,
    val status: Int?
)