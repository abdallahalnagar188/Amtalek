package eramo.amtalek.data.remote.dto.drawer.myaccount


import com.google.gson.annotations.SerializedName

data class MgetProfile(
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
        @SerializedName("dashboard_link")
        val dashboardLink: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("has_package")
        val hasPackage: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("last_name")
        val lastName: String?,
        @SerializedName("phone")
        val phone: String?
    )
}