package eramo.amtalek.data.remote.dto.drawer.preview

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.preview.RoomTypesModel
import eramo.amtalek.util.LocalUtil

data class RoomTypesResponse(
    @SerializedName("room_types") var roomTypeDtos: ArrayList<RoomTypesDto> = arrayListOf()
)

data class RoomTypesDto(
    @SerializedName("room_id") var roomId: String? = null,
    @SerializedName("room_en_name") var roomEnName: String? = null,
    @SerializedName("room_ar_name") var roomArName: String? = null
) {
    fun toRoomTypesModel(): RoomTypesModel {
        return RoomTypesModel(
            roomId = roomId ?: "",
            roomName = if (LocalUtil.isEnglish()) roomEnName ?: "" else roomArName ?: ""
        )
    }
}