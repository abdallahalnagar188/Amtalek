package eramo.amtalek.data.remote.dto.broker.entity

import eramo.amtalek.data.remote.dto.broker.entity.Data

data class BrokersResponse(
    val `data`: Data,
    val message: String,
    val status: Int
)