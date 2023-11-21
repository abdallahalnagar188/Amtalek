package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.ContactUsInfoModel

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

    fun toContactUsInfoModel(): ContactUsInfoModel {
        return ContactUsInfoModel(
            data?.get(0)?.address ?: "",
            data?.get(0)?.phone ?: "",
            data?.get(0)?.mail ?: "",
            data?.get(0)?.facebook ?: "",
            data?.get(0)?.twitter ?: "",
            data?.get(0)?.instagram ?: "",
            data?.get(0)?.linkedIn ?: "",
            data?.get(0)?.youtube ?: ""
        )
    }
}