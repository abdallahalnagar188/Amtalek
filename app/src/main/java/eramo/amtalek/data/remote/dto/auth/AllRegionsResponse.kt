package eramo.amtalek.data.remote.dto.auth

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.RegionsModel
import eramo.amtalek.util.LocalUtil

data class AllRegionsResponse(
    @SerializedName("all_regions") var allRegions: ArrayList<AllRegions> = arrayListOf()
)

data class AllRegions(
    @SerializedName("main_id") var mainId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("name_ar") var nameAr: String? = null,
    @SerializedName("from_id") var fromId: String? = null,
    @SerializedName("from_type") var fromType: String? = null,
    @SerializedName("created") var created: String? = null,
    @SerializedName("updated") var updated: String? = null
) {
    fun toRegionsModel(): RegionsModel {
        return RegionsModel(
            mainId = mainId ?: "",
            name = if (LocalUtil.isEnglish()) name ?: "" else nameAr ?: "",
            fromId = fromId ?: "",
            fromType = fromType ?: "",
            created = created ?: "",
            updated = updated ?: "",
        )
    }
}