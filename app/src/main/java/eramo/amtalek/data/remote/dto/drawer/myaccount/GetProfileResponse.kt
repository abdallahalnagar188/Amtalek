package eramo.amtalek.data.remote.dto.drawer.myaccount


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.UserModel

data class GetProfileResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("bio")
        val bio: String?,
        @SerializedName("city")
        val city: Int?,
        @SerializedName("city_name")
        val cityName: String?,
        @SerializedName("country")
        val country: Int?,
        @SerializedName("country_name")
        val countryName: String?,
        @SerializedName("cover")
        val cover: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("last_name")
        val lastName: String?,
        @SerializedName("phone")
        val phone: String?
    )

    fun toUserModel(): UserModel {
        return UserModel(
            data?.id ?: -1,
            "",
            data?.firstName ?: "",
            data?.lastName ?: "",
            data?.phone ?: "",
            data?.email ?: "",
            data?.country ?: -1,
            data?.countryName ?: "",
            data?.city ?: -1,
            data?.cityName ?: "",
            data?.bio ?: "",
            data?.image ?: "",
            data?.cover ?: ""
        )
    }
}