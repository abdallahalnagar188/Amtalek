package eramo.amtalek.data.remote.dto.general

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("user_type") var userType: String? = null,
    @SerializedName("accepted_client") var acceptedClient: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("user_address") var userAddress: String? = null,
    @SerializedName("country_id") var country_id: String? = null,
    @SerializedName("city_id") var city_id: String? = null,
    @SerializedName("region_id") var region_id: String? = null,
    @SerializedName("country_name_en") var countryNameEn: String? = null,
    @SerializedName("country_name_ar") var countryNameAr: String? = null,
    @SerializedName("city_name_en") var cityNameEn: String? = null,
    @SerializedName("city_name_ar") var cityNameAr: String? = null,
    @SerializedName("region_name_en") var regionNameEn: String? = null,
    @SerializedName("region_name_ar") var regionNameAr: String? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("user_email") var userEmail: String? = null,
    @SerializedName("user_phone") var userPhone: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("user_pass") var userPass: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("m_image") var mImage: String? = null,
    @SerializedName("active") var active: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("date_send") var dateSend: String? = null,
    @SerializedName("time_send") var timeSend: String? = null,
    @SerializedName("rand_key") var randKey: String? = null,
    @SerializedName("user_city") var userCity: String? = null,
    @SerializedName("user_from") var userFrom: String? = null
) : Parcelable