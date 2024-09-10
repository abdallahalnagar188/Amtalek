package eramo.amtalek.data.remote.dto.brokersProperties.newBrokerProps


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("first")
    var first: String?,
    @SerializedName("last")
    var last: String?,
    @SerializedName("next")
    var next: String?,
    @SerializedName("prev")
    var prev: Any?
)