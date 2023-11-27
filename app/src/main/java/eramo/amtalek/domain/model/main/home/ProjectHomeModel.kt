package eramo.amtalek.domain.model.main.home

data class ProjectHomeModel(
    val id: Int,
    val imageUrl: String,
    val isFavourite: Int,
    val title: String,
    val description: String,
    val location: String,
    val datePosted: String,
    val brokerLogoUrl: String
)