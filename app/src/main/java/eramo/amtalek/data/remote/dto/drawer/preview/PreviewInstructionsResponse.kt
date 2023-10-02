package eramo.amtalek.data.remote.dto.drawer.preview

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.preview.PreviewInstructionModel
import eramo.amtalek.util.LocalUtil

data class PreviewInstructionsResponse(
    @SerializedName("mo3ayna_instruction") var previewInstructionDto: PreviewInstructionDto? = PreviewInstructionDto()
)

data class PreviewInstructionDto(
    @SerializedName("main_id") var mainId: String? = null,
    @SerializedName("title_en") var titleEn: String? = null,
    @SerializedName("title_ar") var titleAr: String? = null
) {
    fun toPreviewInstructionModel(): PreviewInstructionModel {
        return PreviewInstructionModel(
            mainId = mainId ?: "",
            title = if (LocalUtil.isEnglish()) titleEn ?: "" else titleAr ?: ""
        )
    }
}