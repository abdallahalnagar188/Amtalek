package eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker


import com.google.gson.annotations.SerializedName

data class SubmitToBrokerResponse(
    @SerializedName("data")
    var `data`: Any?,  // Keep it as a list
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)

