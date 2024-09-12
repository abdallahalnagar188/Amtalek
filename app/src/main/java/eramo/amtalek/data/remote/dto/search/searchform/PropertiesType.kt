package eramo.amtalek.data.remote.dto.search.searchform


import com.google.gson.annotations.SerializedName

data class PropertiesType(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("properties_count")
    var propertiesCount: Int?,
    @SerializedName("title")
    var title: String?
)