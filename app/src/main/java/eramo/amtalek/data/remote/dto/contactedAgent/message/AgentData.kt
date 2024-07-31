package eramo.amtalek.data.remote.dto.contactedAgent.message


import com.google.gson.annotations.SerializedName

data class AgentData(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: Any?,
    @SerializedName("messages")
    var messages: MutableList<Message?>?,
    @SerializedName("name")
    var name: String?
)