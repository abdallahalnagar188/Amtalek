package eramo.amtalek.domain.model.main

data class ProjectModel(
    val imageUrl: String,
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val isFavourite: Int,
    val brokerName: String,
    val brokerLogoUrl: String
)