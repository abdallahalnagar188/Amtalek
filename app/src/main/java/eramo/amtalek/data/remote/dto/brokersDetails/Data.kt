package eramo.amtalek.data.remote.dto.brokersDetails

data class Data(
    val broker_type: String?,
    val description: String?,
    val email: String?,
    val has_package: String?,
    val id: Int?,
    val logo: String?,
    val name: String?,
    val phone: String?,
    val projects: List<Project?>?,
    val projects_count: Int?,
    val property_for_rent: Int?,
    val property_for_sale: Int?
)