package eramo.amtalek.data.remote.dto.auth


import com.google.gson.annotations.SerializedName

data class MCitiesResponse(
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
        @SerializedName("title")
        val title: String?
    )
}