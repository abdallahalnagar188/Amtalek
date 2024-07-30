package eramo.amtalek.data.remote.dto.contactedAgent


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("actor_type")
    var actorType: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("message_type")
    var messageType: String?,
    @SerializedName("name")
    var name: String?
)