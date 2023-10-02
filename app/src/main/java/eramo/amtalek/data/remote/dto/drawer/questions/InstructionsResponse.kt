package eramo.amtalek.data.remote.dto.drawer.questions

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.questions.InstructionsModel
import eramo.amtalek.util.LocalUtil

data class InstructionsResponse(
    @SerializedName("instructions") var instructions: ArrayList<InstructionsDto> = arrayListOf()
)

data class InstructionsDto(
    @SerializedName("type_id") var typeId: String? = null,
    @SerializedName("title_ar") var titleAr: String? = null,
    @SerializedName("title_en") var titleEn: String? = null,
    @SerializedName("desc_en") var descEn: String? = null,
    @SerializedName("desc_ar") var descAr: String? = null
) {
    fun toInstructionsModel(): InstructionsModel {
        return InstructionsModel(
            typeId = typeId ?: "",
            title = if (LocalUtil.isEnglish()) titleEn ?: "" else titleAr ?: "",
            desc = if (LocalUtil.isEnglish()) descEn ?: "" else descAr ?: ""
        )
    }
}