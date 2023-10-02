package eramo.amtalek.data.remote.dto.products

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.products.AdsModel
import eramo.amtalek.util.LocalUtil

data class AdsDto(
    @SerializedName("ads_id") var adsId: String? = null,
    @SerializedName("ads_in") var adsIn: String? = null,
    @SerializedName("client_type") var clientType: String? = null,
    @SerializedName("adv_name") var advName: String? = null,
    @SerializedName("ads_location") var adsLocation: String? = null,
    @SerializedName("adv_date") var advDate: String? = null,
    @SerializedName("adv_time") var advTime: String? = null,
    @SerializedName("space_type") var spaceType: String? = null,
    @SerializedName("client_id_fk") var clientIdFk: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("iframe_text") var iframeText: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("num_views") var numViews: String? = null,
    @SerializedName("num_click") var numClick: String? = null,
    @SerializedName("from_date") var fromDate: String? = null,
    @SerializedName("from_date_s") var fromDateS: String? = null,
    @SerializedName("to_date") var toDate: String? = null,
    @SerializedName("to_date_s") var toDateS: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("agent_brok_fk") var agentBrokFk: String? = null,
    @SerializedName("parent_user") var parentUser: String? = null,
    @SerializedName("action_date") var actionDate: String? = null,
    @SerializedName("action_time") var actionTime: String? = null,
    @SerializedName("action_user") var actionUser: String? = null,
    @SerializedName("action_view") var actionView: String? = null,
    @SerializedName("ads_url") var adsUrl: String? = null,
    @SerializedName("main_title_en") var mainTitleEn: String? = null,
    @SerializedName("main_title_ar") var mainTitleAr: String? = null,
    @SerializedName("short_title_en") var shortTitleEn: String? = null,
    @SerializedName("short_title_ar") var shortTitleAr: String? = null
) {
    fun toAdsModel(): AdsModel {
        return AdsModel(
            adsId = adsId ?: "",
            adsIn = adsIn ?: "",
            clientType = clientType ?: "",
            advName = advName ?: "",
            adsLocation = adsLocation ?: "",
            advDate = advDate ?: "",
            advTime = advTime ?: "",
            spaceType = spaceType ?: "",
            clientIdFk = clientIdFk ?: "",
            type = type ?: "",
            iframeText = iframeText ?: "",
            image = image ?: "",
            numViews = numViews ?: "",
            numClick = numClick ?: "",
            fromDate = fromDate ?: "",
            fromDateS = fromDateS ?: "",
            toDate = toDate ?: "",
            toDateS = toDateS ?: "",
            status = status ?: "",
            userId = userId ?: "",
            agentBrokFk = agentBrokFk ?: "",
            parentUser = parentUser ?: "",
            actionDate = actionDate ?: "",
            actionTime = actionTime ?: "",
            actionUser = actionUser ?: "",
            actionView = actionView ?: "",
            adsUrl = adsUrl ?: "",
            mainTitle = if (LocalUtil.isEnglish()) mainTitleEn ?: "" else mainTitleAr ?: "",
            shortTitle = if (LocalUtil.isEnglish()) shortTitleEn ?: "" else shortTitleAr ?: ""
        )
    }
}