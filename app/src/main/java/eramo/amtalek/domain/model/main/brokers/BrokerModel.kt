package eramo.amtalek.domain.model.main.brokers

data class BrokerModel(
    val imageUrl: String,
    val title: String,
    val body: String,
    val propertiesCount: Int,
    val residentialProjectsCount: Int
)