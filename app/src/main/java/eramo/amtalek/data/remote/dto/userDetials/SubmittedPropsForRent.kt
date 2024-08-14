package eramo.amtalek.data.remote.dto.userDetials

import android.os.Parcelable
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import kotlinx.parcelize.Parcelize

data class SubmittedPropsForRent(
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
    val sale_price: String?,
    val title: String?,
    val twitter: String?
)
//    fun toPropertyModel():PropertyModel{
//        return PropertyModel(
//            id = id?:0,
//            imageUrl = primary_image?:"",
//            title = title?:"",
//            type = for_what?:"",
//            isFavourite = is_fav?:"",
//            rentPrice = rent_price?:0,
//            sellPrice = 0,
//            bedsCount = bed_rooms_no?:0,
//            bathroomsCount = bath_room_no?:0,
//            area = land_area?:0,
//            brokerId = (broker_details?.get(0)?.id?:0).toString(),
//            brokerLogoUrl = broker_details?.get(0)?.logo?:"",
//            location = "$city, $region",
//            datePosted = created_at?:"",
//            isFeatured = property_type?:"",
//            rentDuration = rent_duration?:"",
//            listingNumber = listing_number?:"",
//            currency = currency?:"",
//            acceptance = "",
//            sold = false,
//            offerData = null,
//            region = region?:"",
//            subRegion = "",
//            vendorType = broker_details?.get(0)?.broker_type?:"",
//            senderName = broker_details?.get(0)?.name?:"",
//            senderPhone = "",
//            offerPrice = "",
//            offerDate = "",
//
//            )
//    }
