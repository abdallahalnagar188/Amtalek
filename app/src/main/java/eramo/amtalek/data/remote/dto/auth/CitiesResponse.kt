package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.auth.CityModel

data class CitiesResponse(
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
        @SerializedName("image")
        val image: String?,
        @SerializedName("properties")
        val properties: Int?,
        @SerializedName("rent_properties")
        val rentProperties: Int?,
        @SerializedName("sale_properties")
        val saleProperties: Int?,
        @SerializedName("title_en")
        val titleEn: String?,
        @SerializedName("title_ar")
        val titleAr: String?,
    ) {
        fun toCityModel(): CityModel {
            return CityModel(id ?: -1, titleEn = titleEn ?: "", titleAr = titleAr?:"", image = image?:"", properties = properties?:-1, rentProperties = rentProperties?:-1, saleProperties = saleProperties?:-1)
        }
    }
}