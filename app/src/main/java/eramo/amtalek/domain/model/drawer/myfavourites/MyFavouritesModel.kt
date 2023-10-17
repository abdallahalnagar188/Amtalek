package eramo.amtalek.domain.model.drawer.myfavourites

data class MyFavouritesModel(
    val imageUrl: String,
    val type: String,
    val isFavourite: Int,
    val isFeatured: Int,
    val price: Double,
    val title: String,
    val area: Int,
    val bathroomsCount: Int,
    val bedsCount: Int,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String
)
