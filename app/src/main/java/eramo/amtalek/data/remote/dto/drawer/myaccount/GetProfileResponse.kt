package eramo.amtalek.data.remote.dto.drawer.myaccount


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.GetProfileModel
import kotlinx.parcelize.Parcelize
@Parcelize
data class GetProfileResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) : Parcelable {
    @Parcelize
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
    ) : Parcelable

    fun toGetProfileModel(): GetProfileModel {
        return GetProfileModel(
            id =  data?.id ?: -1,
            city =  data?.city ?: -1,
            cityName = data?.cityName ?: "",
            country =   data?.country ?: -1,
            countryName =  data?.countryName ?: "",
            cover = data?.cover ?: "",
            email =  data?.email ?: "",
            firstName = data?.firstName ?: "",
            bio = data?.bio ?: "",
            image = data?.image ?: "",
            lastName = data?.lastName ?: "",
            phone = data?.phone ?: "",
            dashboardLink = data?.dashboardLink?:"",
            hasPackage = data?.hasPackage?:""
        )
    }
}