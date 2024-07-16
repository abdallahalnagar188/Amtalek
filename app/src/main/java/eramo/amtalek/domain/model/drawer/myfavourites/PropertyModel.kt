package eramo.amtalek.domain.model.drawer.myfavourites

import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.OfferData

data class PropertyModel(
    val id:Int,
    val imageUrl: String,
    val type: String,
    val isFavourite: String,
    val isFeatured: String,
    val rentPrice: Int,
    val sellPrice: Int,
    val title: String,
    val area: Int,
    val bathroomsCount: Int,
    val bedsCount: Int,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String,
    val brokerId:String,
    val rentDuration:String,
    val listingNumber:String,
    val currency:String,
    val acceptance:String,
    val sold:Boolean,
    val offerData: OfferData?,
    val region:String,
    val subRegion:String,
//    val totalProps:String?,
    )
