package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName

data class ContactUsResponse(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: List<Data?>?
) {
    data class Data(
        @SerializedName("location")
        val location: String?,
        @SerializedName("address")
        val address: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("mail")
        val mail: String?,
        @SerializedName("facebook")
        val facebook: String?,
        @SerializedName("twitter")
        val twitter: String?,
        @SerializedName("instagram")
        val instagram: String?,
        @SerializedName("linkedIn")
        val linkedIn: String?,
        @SerializedName("youtube")
        val youtube: String?
    )
}