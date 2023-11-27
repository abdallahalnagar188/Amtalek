package eramo.amtalek.domain.model.main.home

data class PropertiesByCityModel(
    val id: Int,
    val imageUrl: String,
    val cityName: String,
    val forSellCount: Int,
    val forRentCount: Int
)