package eramo.amtalek.data.remote.dto.userDetials

import android.os.Parcelable

data class Data(
    val broker_type: String?,
    val description: String?,
    val email: String?,
    val first_name: String?,
    val has_package: String?,
    val id: Int?,
    val last_name: String?,
    val logo: String?,
    val name: String?,
    val offers: List<String>?,
    val phone: String?,
    val src: String?,
    val submitted_props_for_rent: List<SubmittedPropsForRent>?,
    val submitted_props_for_sale: List<SubmittedPropsForSale>?,
)