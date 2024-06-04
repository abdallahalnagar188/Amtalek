package eramo.amtalek.data.remote.dto.editprofile


import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("data")
    var `data`: List<Any?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)