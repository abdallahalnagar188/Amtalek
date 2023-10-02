package eramo.amtalek.data.remote.dto.drawer

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.PolicyInfoModel
import eramo.amtalek.util.LocalUtil

data class PolicyInfoResponse(
    @SerializedName("policy_info") var policyInfoDto: ArrayList<PolicyInfoDto> = arrayListOf()
)

data class PolicyInfoDto(
    @SerializedName("id_config") var idConfig: String? = null,
    @SerializedName("policy_en") var policyEn: String? = null,
    @SerializedName("policy_ar") var policyAr: String? = null
) {
    fun toPolicyInfoModel(): PolicyInfoModel {
        return PolicyInfoModel(
            idConfig = idConfig ?: "",
            policy = if (LocalUtil.isEnglish()) policyEn ?: "" else policyAr ?: ""
        )
    }
}