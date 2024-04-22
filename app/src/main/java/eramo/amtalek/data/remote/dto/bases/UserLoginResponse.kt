package eramo.amtalek.data.remote.dto.bases


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.UserModel


data class GeneralLoginResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("token")
    val token: String?
){
    fun toUserModel():UserModel{
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
            countryName = data?.countryName?:"",
            hasCity = data?.hasCity,
            hasCountry =data?.hasCity,
            hasRegion =data?.hasRegion,
            address = data?.hasRegion?:"",
            hrId = data?.hrId?:"",
            hrAcceptance =data?.hrAcceptance?:-1 ,
            branchId =data?.branchId?:"" ,
            attendance =data?.attendance?:-1 ,
            calls = data?.calls?:-1,
            deptId = data?.deptId?:-1,
            deviceToken = data?.deviceToken?:"",
            doneTasks = data?.doneTasks?:-1,
            deletedAt =data?.deletedAt?:"" ,
            leads = data?.leads?:-1,
            leaderId =data?.leaderId?:"",
            regionName = data?.regionName?:"",
            yearlyVacations = data?.yearlyVacations?:-1,
            sickVacations =data?.sickVacations?:-1 ,
            meetings = data?.meetings?:-1,
            name = data?.name?:"",
            salary =data?.salary?:"" ,
            roleId =data?.roleId?:"" ,
            ceoAcceptance =data?.ceoAcceptance?:-1 ,
            companyId = data?.companyId?:-1,
            commission =data?.commission?:-1,
            password = data?.password?:"",
            idNumber = data?.idNumber?:"",
            image = data?.image?:"",
            prefix = data?.prefix?:"" )
    }
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
    @SerializedName("city_name")
    val cityName: String?,
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
    @SerializedName("country_name")
    val countryName: String?,
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
    @SerializedName("has_city")
    val hasCity: HasCity?,
    @SerializedName("has_country")
    val hasCountry: HasCountry?,
    @SerializedName("has_package")
    val hasPackage: String?,
    @SerializedName("has_region")
    val hasRegion: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("region")
    val region: Int?,
    @SerializedName("region_name")
    val regionName: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_image")
    val userImage: String?,
    @SerializedName("verified")
    val verified: String?,
    //=============================
    @SerializedName("address")
    val address: String?,
    @SerializedName("attendance")
    val attendance: Int?,
    @SerializedName("branch_id")
    val branchId: String?,
    @SerializedName("calls")
    val calls: Int?,
    @SerializedName("ceo_acceptance")
    val ceoAcceptance: Int?,
    @SerializedName("commission")
    val commission: Int?,
    @SerializedName("company_id")
    val companyId: Int?,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    @SerializedName("dept_id")
    val deptId: Int?,
    @SerializedName("device_token")
    val deviceToken: String?,
    @SerializedName("done_tasks")
    val doneTasks: Int?,
    @SerializedName("hr_acceptance")
    val hrAcceptance: Int?,
    @SerializedName("hr_id")
    val hrId: String?,
    @SerializedName("id_number")
    val idNumber: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("leader_id")
    val leaderId: String?,
    @SerializedName("leads")
    val leads: Int?,
    @SerializedName("meetings")
    val meetings: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("prefix")
    val prefix: String?,
    @SerializedName("role_id")
    val roleId: String?,
    @SerializedName("salary")
    val salary: String?,
    @SerializedName("sick_vacations")
    val sickVacations: Int?,
    @SerializedName("yearly_vacations")
    val yearlyVacations: Int?,
)
data class HasCity(
    @SerializedName("country_id")
    val countryId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: Any?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("title")
    val title: Title?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
data class HasCountry(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: Any?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("title")
    val title: Title?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
data class Title(
    @SerializedName("ar")
    val ar: String?,
    @SerializedName("en")
    val en: String?
)
