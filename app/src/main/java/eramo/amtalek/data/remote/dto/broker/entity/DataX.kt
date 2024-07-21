package eramo.amtalek.data.remote.dto.broker.entity

data class DataX(
    val broker_type: String,
    val description: String,
    val id: Int,
    val logo: String,
    val name: String,
    val properties_count: Int,
    val property_for_rent: Int,
    val property_for_sale: Int
)