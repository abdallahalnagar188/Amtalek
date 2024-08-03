package eramo.amtalek.data.remote.dto.userDetials

data class UserDetailsResponse(
    val `data`: List<Data>?,
    val message: String?,
    val status: Int?
)