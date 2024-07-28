package eramo.amtalek.data.remote.dto.property.allproperty


import com.google.gson.annotations.SerializedName

data class AllPropertyResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)