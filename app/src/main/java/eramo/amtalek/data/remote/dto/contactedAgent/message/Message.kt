package eramo.amtalek.data.remote.dto.contactedAgent.message


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("link")
    var link: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("message_time")
    var messageTime: String?,
    @SerializedName("message_type")
    var messageType: String?
)