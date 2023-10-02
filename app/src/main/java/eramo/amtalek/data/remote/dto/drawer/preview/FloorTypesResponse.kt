package eramo.amtalek.data.remote.dto.drawer.preview

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.preview.FloorTypesModel
import eramo.amtalek.util.LocalUtil

data class FloorTypesResponse(
    @SerializedName("floor_types") var floorTypeDtos: ArrayList<FloorTypesDto> = arrayListOf()
)

data class FloorTypesDto(
    @SerializedName("floor_id") var floorId: String? = null,
    @SerializedName("floor_en_name") var floorEnName: String? = null,
    @SerializedName("floor_ar_name") var floorArName: String? = null
) {
    fun toFloorTypes(): FloorTypesModel {
        return FloorTypesModel(
            floorId = floorId ?: "",
            floorName = if (LocalUtil.isEnglish()) floorEnName ?: "" else floorArName ?: ""
        )
    }
}