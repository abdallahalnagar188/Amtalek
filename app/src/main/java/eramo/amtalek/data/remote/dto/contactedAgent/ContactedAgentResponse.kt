package eramo.amtalek.data.remote.dto.contactedAgent


import com.google.gson.annotations.SerializedName

data class ContactedAgentResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)