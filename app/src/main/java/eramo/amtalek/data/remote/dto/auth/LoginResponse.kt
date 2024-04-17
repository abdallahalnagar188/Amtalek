package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.UserModel

data class LoginResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("data")
        val `data`: Data?,
        @SerializedName("token")
        val token: String?
    ) {
        fun toUserModel(): UserModel {
            return UserModel(
               actorType =  data?.actorType ?: "",
                bio = data?.bio ?: "",
                token = token ?: "",
                birthday = data?.birthday ?: "",
                city = data?.city ?: -1,
                code = data?.code ?: "",
                codeExpiredDate = data?.codeExpiredDate ?: "",
                codeSource = data?.codeSource ?: "",
                codeUsage = data?.codeUsage ?: "",
                country = data?.country?: -1,
                createdAt = data?.createdAt ?: "",
                createdFrom = data?.createdFrom ?: "",
                dashboardLink = data?.dashboardLink?:"",
                email =data?.email?:"" ,
                emailVerifiedAt = data?.emailVerifiedAt?:"",
                firebaseToken = data?.firebaseToken?:"",
                firstName = data?.firstName?:"",
                gender = data?.gender?:"",
                hasPackage =data?.hasPackage?:"" ,
                id =data?.id?:-1 ,
                lastName =data?.lastName?:"" ,
                phone = data?.phone?:"",
                region = data?.region?:-1,
                status = data?.status?:"",
                updatedAt =data?.updatedAt?:"",
                userImage =data?.userImage?:"" ,
                verified =data?.verified?:"" ,
                cityName =data?.cityName?:"",
                countryName = data?.countryName?:""
            )
        }
        data class Data(
            @SerializedName("actor_type")
            val actorType: String?,
            @SerializedName("bio")
            val bio: String?,
            @SerializedName("birthday")
            val birthday: String?,
            @SerializedName("city")
            val city: Int?,
            @SerializedName("code")
            val code: String?,
            @SerializedName("code_expired_date")
            val codeExpiredDate: String?,
            @SerializedName("code_source")
            val codeSource: String?,
            @SerializedName("code_usage")
            val codeUsage: String?,
            @SerializedName("country")
            val country: Int?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("created_from")
            val createdFrom: String?,
            @SerializedName("dashboard_link")
            val dashboardLink: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("email_verified_at")
            val emailVerifiedAt: String?,
            @SerializedName("firebase_token")
            val firebaseToken: String?,
            @SerializedName("first_name")
            val firstName: String?,
            @SerializedName("gender")
            val gender: String?,
            @SerializedName("has_package")
            val hasPackage: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("last_name")
            val lastName: String?,
            @SerializedName("phone")
            val phone: String?,
            @SerializedName("region")
            val region: Int?,
            @SerializedName("status")
            val status: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("user_image")
            val userImage: String?,
            @SerializedName("verified")
            val verified: String?,
            @SerializedName("city_name")
            val cityName: String? ,
            @SerializedName("country_name")
            val countryName: String?
        )
    }
}