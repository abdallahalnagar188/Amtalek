package eramo.amtalek.data.remote.dto.userDetials.response


import com.google.gson.annotations.SerializedName

data class Description(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
)