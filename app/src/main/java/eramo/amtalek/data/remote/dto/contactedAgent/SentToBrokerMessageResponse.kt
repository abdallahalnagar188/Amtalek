package eramo.amtalek.data.remote.dto.contactedAgent


import com.google.gson.annotations.SerializedName

data class SentToBrokerMessageResponse(
    @SerializedName("data")
    var `data`: DataX?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)