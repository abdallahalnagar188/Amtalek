package eramo.amtalek.data.remote.dto.packages


import com.google.gson.annotations.SerializedName

data class SubscribeToPackageResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)