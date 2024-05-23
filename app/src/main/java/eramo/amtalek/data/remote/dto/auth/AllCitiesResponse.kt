package eramo.amtalek.data.remote.dto.auth

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.CitiesModelx
import eramo.amtalek.util.LocalUtil

data class AllCitiesResponse(
    @SerializedName("all_cities") var allCities: ArrayList<AllCities> = arrayListOf()
)

data class AllCities(
    @SerializedName("main_id") var mainId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("name_ar") var nameAr: String? = null,
    @SerializedName("from_id") var fromId: String? = null,
    @SerializedName("from_type") var fromType: String? = null,
    @SerializedName("created") var created: String? = null,
    @SerializedName("updated") var updated: String? = null
){
    fun toCitiesModel(): CitiesModelx {
        return CitiesModelx(
            mainId = mainId ?: "",
            name = if (LocalUtil.isEnglish()) name ?: "" else nameAr ?: "",
            fromId = fromId ?: "",
            fromType = fromType ?: "",
            created = created ?: "",
            updated = updated ?: "",
        )
    }
}