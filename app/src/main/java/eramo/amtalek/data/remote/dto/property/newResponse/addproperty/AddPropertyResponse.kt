package eramo.amtalek.data.remote.dto.property.newResponse.addproperty


import com.google.gson.annotations.SerializedName

data class AddPropertyResponse(
    @SerializedName("data")
    var `data`: List<Any?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)