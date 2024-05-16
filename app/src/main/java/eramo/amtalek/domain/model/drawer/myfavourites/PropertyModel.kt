package eramo.amtalek.domain.model.drawer.myfavourites

data class PropertyModel(
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
    val rentDuration:String
)
