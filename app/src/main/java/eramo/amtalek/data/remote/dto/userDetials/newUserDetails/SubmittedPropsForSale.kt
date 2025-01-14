package eramo.amtalek.data.remote.dto.userDetials.newUserDetails

data class SubmittedPropsForSale(
    val address: String?,
    val amenities: List<Amenity?>?,
    val bath_room_no: Int?,
    val bed_rooms_no: Int?,
    val broker_details: List<BrokerDetail?>?,
    val category: String?,
    val city: String?,
    val country: String?,
    val created_at: String?,
    val currency: String?,
    val description: String?,
    val facebook: String?,
    val for_what: String?,
    val google_plus: String?,
    val id: Int?,
    val is_fav: String?,
    val land_area: Int?,
    val listing_number: String?,
    val normal_featured: String?,
    val primary_image: String?,
    val property_type: String?,
    val region: String?,
    val rent_duration: String?,
    val rent_price: Int?,
    val room_ensuite: Int?,
    val sale_price: Int?,
    val title: String?,
    val twitter: String?
)