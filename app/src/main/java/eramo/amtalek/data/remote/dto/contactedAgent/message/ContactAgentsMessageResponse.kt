package eramo.amtalek.data.remote.dto.contactedAgent.message


import com.google.gson.annotations.SerializedName

data class ContactAgentsMessageResponse(
    @SerializedName("data")
    var `data`: DataX?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)