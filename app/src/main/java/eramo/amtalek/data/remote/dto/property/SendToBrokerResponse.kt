package eramo.amtalek.data.remote.dto.property


import com.google.gson.annotations.SerializedName

data class SendToBrokerResponse(
    @SerializedName("data")
    var `data`: List<Any?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)