package eramo.amtalek.data.remote.dto.brokersDetails

data class BrokersDetailsResponse(
    val `data`: List<Data?>?,
    val message: String?,
    val status: Int?
)