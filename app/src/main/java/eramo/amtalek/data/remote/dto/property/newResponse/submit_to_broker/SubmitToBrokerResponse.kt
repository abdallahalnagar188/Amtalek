package eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker


import com.google.gson.annotations.SerializedName

data class SubmitToBrokerResponse(
    @SerializedName("data")
    var `data`: List<Any?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)