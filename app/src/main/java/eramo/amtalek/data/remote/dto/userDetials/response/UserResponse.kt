package eramo.amtalek.data.remote.dto.userDetials.response


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)