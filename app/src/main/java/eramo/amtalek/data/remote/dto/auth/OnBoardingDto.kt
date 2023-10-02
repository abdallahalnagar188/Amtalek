package eramo.amtalek.data.remote.dto.auth

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.OnBoardingModel
import eramo.amtalek.util.LocalUtil

data class OnBoardingDto(
    @SerializedName("all_screens") var allScreens: ArrayList<AllScreens> = arrayListOf()
)

data class AllScreens(
    @SerializedName("spalsh_id") var spalshId: String? = null,
    @SerializedName("spalsh_title_en") var spalshTitleEn: String? = null,
    @SerializedName("spalsh_title_ar") var spalshTitleAr: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("spalsh_details_en") var spalshDetailsEn: String? = null,
    @SerializedName("spalsh_details_ar") var spalshDetailsAr: String? = null
) {
    fun toOnBoardingModel(): OnBoardingModel {
        return OnBoardingModel(
            splashId = spalshId,
            splashTitle = if (LocalUtil.isEnglish()) spalshTitleEn else spalshTitleAr,
            image = image,
            splashDetails = if (LocalUtil.isEnglish()) spalshDetailsEn else spalshDetailsAr
        )
    }
}