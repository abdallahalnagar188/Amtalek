package eramo.amtalek.domain.model.auth

data class CityModel(
    val id: Int,
    val titleEn: String,
    val titleAr: String,
    val image: String,
    val properties: Int,
    val rentProperties: Int,
    val saleProperties: Int,

    )