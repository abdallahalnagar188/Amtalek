package eramo.amtalek.data.remote.dto.property


import com.google.gson.annotations.SerializedName
import eramo.amtalek.data.remote.dto.contactedAgent.DataX

data class SendToBrokerResponse(
    @SerializedName("data")
    var `data`: DataX?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)