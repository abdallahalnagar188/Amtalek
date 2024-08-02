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

data class DataX(
    @SerializedName("agent_data")
    var agentData: AgentData?
)
data class AgentData(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: Any?,
    @SerializedName("messages")
    var messages: MutableList<Message>,
    @SerializedName("name")
    var name: String?
)
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