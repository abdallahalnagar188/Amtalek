package eramo.amtalek.data.remote.dto.userDetials

import eramo.amtalek.data.remote.dto.userDetials.newUserDetails.Offer

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
    val offers: List<Offer>?,
    val phone: String?,
    val src: String?,
    val submitted_props_for_rent: List<SubmittedPropsForRent>?,
    val submitted_props_for_sale: List<SubmittedPropsForSale>?,
) {
    // Method to sum the two lists into one list
    fun getAllSubmittedProps(): List<Any> {
        // Handle null lists and combine them into one
        val rentList = submitted_props_for_rent ?: emptyList()
        val saleList = submitted_props_for_sale ?: emptyList()

        return rentList + saleList
    }
}
