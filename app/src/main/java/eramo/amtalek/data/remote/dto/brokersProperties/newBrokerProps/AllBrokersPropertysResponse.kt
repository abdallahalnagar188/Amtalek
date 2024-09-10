package eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps


import com.google.gson.annotations.SerializedName

data class AllBrokersPropertysResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)