package eramo.amtalek.data.remote.dto.adons


import com.google.gson.annotations.SerializedName

data class AdonsResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)