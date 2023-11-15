package eramo.amtalek.data.remote.dto.general

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.util.LocalUtil

data class ResultDto(
    @SerializedName("success") var success: Int? = null,
    @SerializedName("message_en") var messageEn: String? = null,
    @SerializedName("message_ar") var messageAr: String? = null
) {
    fun toResultModel(): ResultModel {
        return ResultModel(
            status = success ?: 0,
            message = if (LocalUtil.isEnglish()) messageEn ?: "" else messageAr ?: ""
        )
    }
}