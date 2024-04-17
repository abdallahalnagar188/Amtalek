package eramo.amtalek.domain.model.auth


data class GetProfileModel (
    val bio: String,
    val city: Int,
    val cityName: String,
    val country: Int,
    val countryName: String,
    val cover: String,
    val email: String,
    val firstName: String,
    val id: Int,
    val image: String,
    val lastName: String,
    val phone: String,
    val dashboardLink:String,
    val hasPackage:String,

)