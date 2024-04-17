package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.RegionModel
import eramo.amtalek.domain.model.auth.RegionsModel

data class RegionsResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("title")
        val title: String?
    ){
        fun toRegionModel(): RegionModel {
            return RegionModel(id ?: -1, title ?: "")
        }
    }
}