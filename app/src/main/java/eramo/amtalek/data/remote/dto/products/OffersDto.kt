package eramo.amtalek.data.remote.dto.products

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.OffersModel

data class OffersResponse(
    @SerializedName("partners") var partners: ArrayList<OffersDto> = arrayListOf()
)

data class OffersDto(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("category") var category: String? = null
) {
    fun toOffersModel(): OffersModel {
        return OffersModel(
            id = id ?: "",
            name = name ?: "",
            image = image ?: "",
            url = url ?: "",
            category = category ?: ""
        )
    }
}