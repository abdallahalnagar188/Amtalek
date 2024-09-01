package eramo.amtalek.domain.model.drawer.myfavourites

data class ProjectModel (
    val id: Int,
    val imageUrl: String,
    val isFavourite: String,
    val isFeatured: String,
    val title: String,
    val description: String,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String,
    val brokerId:Int,
    val listingNumber:String,
    val priceFrom:String,
)