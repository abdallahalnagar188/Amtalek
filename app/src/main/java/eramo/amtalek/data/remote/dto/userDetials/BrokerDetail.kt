package eramo.amtalek.data.remote.dto.userDetials

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrokerDetail(
    val broker_type: String?,
    val description: String?,
    val email: String?,
    val first_name: String?,
    val id: Int?,
    val last_name: String?,
    val logo: String?,
    val name: String?,
    val phone: String?,
    val src: String?
): Parcelable