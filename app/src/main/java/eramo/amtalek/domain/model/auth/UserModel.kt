package eramo.amtalek.domain.model.auth


data class UserModel(
    val id: Int,
    val token: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val countryId: Int,
    val countryName: String,
    val cityId: Int,
    val cityName: String,
    val bio: String,
    val profileImageUrl: String,
    val coverImageUrl: String,
)
