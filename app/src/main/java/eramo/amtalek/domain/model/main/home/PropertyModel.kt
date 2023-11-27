package eramo.amtalek.domain.model.main.home

data class PropertyModel(
    val imageUrl: String,
    val type: String,
    val isFavourite: Int,
    val isFeatured: Int,
    val sellPrice: Double,
    val rentPrice: Double,
    val rentDuration: String,
    val title: String,
    val area: Int,
    val bathroomsCount: Int,
    val bedsCount: Int,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String
)
