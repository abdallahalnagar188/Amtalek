package eramo.amtalek.data.remote.dto.myHome.filter_by_city


import com.google.gson.annotations.SerializedName

data class HomeCitiesResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)
data class Data(
    @SerializedName("data")
    var `data`: List<MyData>?,
    @SerializedName("title")
    var title: String?
)
data class MyData(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("properties_count")
    var propertiesCount: Int?,
    @SerializedName("rent_properties")
    var rentProperties: Int?,
    @SerializedName("sales_properties")
    var salesProperties: Int?,
    @SerializedName("title")
    var title: String?
)