package eramo.amtalek.data.remote.dto.myHome.allCitys


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("properties")
    var properties: Int?,
    @SerializedName("rent_properties")
    var rentProperties: Int?,
    @SerializedName("sale_properties")
    var saleProperties: Int?,
    @SerializedName("title_ar")
    var titleAr: String?,
    @SerializedName("title_en")
    var titleEn: String?
)